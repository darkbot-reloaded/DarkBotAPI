package eu.darkbot.impl.managers;

import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GalaxyInfo;
import eu.darkbot.api.game.galaxy.SpinResult;
import eu.darkbot.api.managers.BackpageAPI;
import eu.darkbot.api.managers.EventBrokerAPI;
import eu.darkbot.api.managers.GalaxySpinnerAPI;
import eu.darkbot.impl.galaxy.GalaxyInfoImpl;
import eu.darkbot.util.http.Http;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.util.Optional;

public class GalaxySpinner implements GalaxySpinnerAPI {
    private final byte[] BUFFER = new byte[1024];

    private final BackpageAPI backpage;
    private final GalaxyInfoImpl galaxyInfo;
    private final EventBrokerAPI eventBroker;

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
        return handleRequest(buildHttp("init", 1000, expiryTime));
    }

    @Override
    public Optional<SpinResult> spinGate(@NotNull GalaxyGate gate, boolean multiplier, int spinAmount, int minWait) {
        Http http = buildHttp("multiEnergy" + gate.getParam(), minWait);

        if (getGalaxyInfo().getFreeEnergy() > 0) http.setParam("sample", 1);
        if (multiplier) http.setParam("multiplier", 1);
        if (spinAmount > 4) http.setParam("spinamount", spinAmount);

        boolean success = Boolean.TRUE.equals(handleRequest(http));

        if (success)
            eventBroker.sendEvent(new SpinGateEvent(galaxyInfo.getSpinResult(), spinAmount));

        return success ? Optional.of(galaxyInfo.getSpinResult()) : Optional.empty();
    }

    @Override
    public boolean placeGate(@NotNull GalaxyGate gate, int minWait) {
        boolean success = Boolean.TRUE.equals(handleRequest(buildHttp("setupGate" + gate.getIdParam(), minWait)));
        if (success)
            eventBroker.sendEvent(new PlaceGateEvent(gate));

        return success;
    }

    @Override
    public boolean buyLife(@NotNull GalaxyGate gate, int minWait) {
        return Boolean.TRUE.equals(handleRequest(buildHttp("buyLife" + gate.getIdParam(), minWait)));
    }

    private Http buildHttp(String action, int minWait) {
        return backpage.getHttp("flashinput/galaxyGates.php", minWait)
                .setParam("userID", backpage.getUserId())
                .setParam("sid", backpage.getSid())
                .setParam("action", action);
    }

    private Http buildHttp(String action, int expiryTime, int minWait) {
        if (!backpage.isInstanceValid() || System.currentTimeMillis() <= lastUpdate + expiryTime) return null;
        return buildHttp(action, minWait);
    }

    private Boolean handleRequest(Http request) {
        if (request == null) return null;
        try {
            Document document = getDocument(request);
            if (document == null) return false;

            galaxyInfo.update(document);
            lastUpdate = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Document getDocument(Http http) throws Exception {
        return http.consumeInputStream(is -> {
            try (BufferedInputStream bis = new BufferedInputStream(is)) {
                bis.mark(1024);

                String start = new String(BUFFER, 0, bis.read(BUFFER));
                if (start.equals("materializer locked")) return null;

                bis.reset();

                return DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(bis);
            }
        });
    }
}
