package eu.darkbot.impl.managers;

import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GalaxyInfo;
import eu.darkbot.api.game.galaxy.SpinResult;
import eu.darkbot.api.managers.BackpageAPI;
import eu.darkbot.api.managers.EventBrokerAPI;
import eu.darkbot.api.managers.GalaxySpinnerAPI;
import eu.darkbot.impl.galaxy.GalaxyInfoImpl;
import eu.darkbot.util.TimeUtils;
import eu.darkbot.util.Timer;
import eu.darkbot.util.XmlUtils;
import eu.darkbot.util.http.Http;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Optional;

public class GalaxySpinner implements GalaxySpinnerAPI {
    public static final String API_ENDPOINT = "flashinput/galaxyGates.php";
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_SID = "sid";
    public static final String PARAM_GATE_ID = "gateID";

    private final byte[] buffer = new byte[1024];

    private final BackpageAPI backpage;
    private final GalaxyInfoImpl galaxyInfo;
    private final EventBrokerAPI eventBroker;
    private final Timer infosUpdateTimer = Timer.get(TimeUtils.MINUTE * 3);

    private int spins;
    private long lastUpdate;

    public GalaxySpinner(BackpageAPI backpage, EventBrokerAPI eventBroker) {
        this.backpage = backpage;
        this.eventBroker = eventBroker;
        this.galaxyInfo = new GalaxyInfoImpl();
    }

    @Override
    public @NotNull GalaxyInfo getGalaxyInfo() {
        return galaxyInfo;
    }

    @Override
    public @Nullable Boolean updateGalaxyInfos(int expiryTime) {
        if (!backpage.isInstanceValid() || System.currentTimeMillis() <= lastUpdate + expiryTime) return null;

        Http http = backpage.getHttp(API_ENDPOINT, 1000)
                .setParam(PARAM_USER_ID, backpage.getUserId())
                .setParam(PARAM_ACTION, "init")
                .setParam(PARAM_SID, backpage.getSid());

        Boolean result = handleRequest(http);
        if (Boolean.TRUE.equals(result))
            infosUpdateTimer.activate();

        return result;
    }

    @Override
    public Optional<SpinResult> spinGate(@NotNull GalaxyGate gate, boolean multiplier, int spinAmount, int minWait) {
        Http http = backpage.getHttp(API_ENDPOINT, minWait)
                .setParam(PARAM_USER_ID, backpage.getUserId())
                .setParam(PARAM_ACTION, "multiEnergy")
                .setParam(PARAM_SID, backpage.getSid())
                .setParam(PARAM_GATE_ID, gate.getId())
                .setParam(gate.getName(), "1");

        if (getGalaxyInfo().getFreeEnergy() > 0) http.setParam("sample", 1);
        if (multiplier) http.setParam("multiplier", 1);
        if (spinAmount > 1) http.setParam("spinamount", spinAmount);

        boolean success = Boolean.TRUE.equals(handleRequest(http));

        if (success) {
            spins += spinAmount;
            eventBroker.sendEvent(new SpinGateEvent(galaxyInfo.getSpinResult(), spinAmount));

            if (infosUpdateTimer.isInactive()) {
                // try to update infos every 3min on successful spin
                // to prevent spinning infinitely when client-side only last parts are missing,
                // where server-side gate is completed
                updateGalaxyInfos(-1);
            }
        }

        return success ? Optional.of(galaxyInfo.getSpinResult()) : Optional.empty();
    }

    @Override
    public int getSpinsUsed() {
        return spins;
    }

    @Override
    public boolean placeGate(@NotNull GalaxyGate gate, int minWait) {
        Http http = backpage.getHttp(API_ENDPOINT, minWait)
                .setParam(PARAM_USER_ID, backpage.getUserId())
                .setParam(PARAM_SID, backpage.getSid())
                .setParam(PARAM_ACTION, "setupGate")
                .setParam(PARAM_GATE_ID, gate.getId());

        boolean success = Boolean.TRUE.equals(handleRequest(http));
        if (success)
            eventBroker.sendEvent(new PlaceGateEvent(gate));

        return success;
    }

    @Override
    public boolean buyLife(@NotNull GalaxyGate gate, int minWait) {
        Http http = backpage.getHttp(API_ENDPOINT, minWait)
                .setParam(PARAM_USER_ID, backpage.getUserId())
                .setParam(PARAM_SID, backpage.getSid())
                .setParam(PARAM_GATE_ID, gate.getId())
                .setParam(PARAM_ACTION, "buyLife");

        return Boolean.TRUE.equals(handleRequest(http));
    }

    private Boolean handleRequest(Http request) {
        try {
            Document document = getDocument(request);
            if (document == null) return false;

            galaxyInfo.update(document);
            lastUpdate = System.currentTimeMillis();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Document getDocument(Http http) throws IOException {
        return http.consumeInputStream(is -> {
            try (BufferedInputStream bis = new BufferedInputStream(is)) {
                bis.mark(1024);

                String start = new String(buffer, 0, bis.read(buffer));
                if ("materializer locked".equals(start)) return null;

                bis.reset();

                try {
                    return XmlUtils.parse(bis);
                } catch (SAXException e) {
                    throw new IOException(e);
                }
            }
        });
    }
}
