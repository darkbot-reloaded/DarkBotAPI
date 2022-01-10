package eu.darkbot.impl.managers;

import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GalaxyInfo;
import eu.darkbot.api.game.galaxy.SpinResult;
import eu.darkbot.api.managers.BackpageAPI;
import eu.darkbot.api.managers.GalaxySpinnerAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.impl.galaxy.GalaxyInfoImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.util.Optional;

public class GalaxySpinner implements GalaxySpinnerAPI {
    private final byte[] BUFFER = new byte[1024];

    private final HeroAPI hero;
    private final BackpageAPI backpage;
    private final GalaxyInfoImpl galaxyInfo;

    private long lastUpdate;

    public GalaxySpinner(HeroAPI hero, BackpageAPI backpage) {
        this.hero = hero;
        this.backpage = backpage;
        this.galaxyInfo = new GalaxyInfoImpl();
    }

    @Override
    public @NotNull GalaxyInfo getGalaxyInfo() {
        return galaxyInfo;
    }

    @Override
    public @Nullable Boolean updateGalaxyInfos(int expiryTime) {
        return handleRequest(getParam("init"), expiryTime, 1000);
    }

    @Override
    public Optional<SpinResult> spinGate(@NotNull GalaxyGate gate, boolean multiplier, int spinAmount, int minWait) {
        String params = getParam("multiEnergy") + gate.getParam();

        if (getGalaxyInfo().getFreeEnergy() > 0) params += "&sample=1";
        if (multiplier) params += "&multiplier=1";
        if (spinAmount > 4) params += "&spinamount=" + spinAmount;

        return Boolean.TRUE.equals(handleRequest(params, -1, minWait))
                ? Optional.of(galaxyInfo.getSpinResult()) : Optional.empty();
    }

    @Override
    public boolean placeGate(@NotNull GalaxyGate gate, int minWait) {
        return Boolean.TRUE.equals(handleRequest(getParam("setupGate") + gate.getIdParam(), -1, minWait));
    }

    @Override
    public boolean buyLife(@NotNull GalaxyGate gate, int minWait) {
        return Boolean.TRUE.equals(handleRequest(getParam("buyLife") + gate.getIdParam(), -1, minWait));
    }

    private String getParam(String action) {
        return "flashinput/galaxyGates.php?userID=" + hero.getId() +
                "&sid=" + backpage.getSid() +
                "&action=" + action;
    }

    private Boolean handleRequest(String params, int expiryTime, int minWait) {
        if (!backpage.isInstanceValid() || System.currentTimeMillis() <= lastUpdate + expiryTime) return null;
        try {
            Document document = getDocument(backpage.getConnection(params, minWait));
            if (document == null) return false;

            galaxyInfo.update(document);
            lastUpdate = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Document getDocument(HttpURLConnection conn) throws Exception {
        try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream())) {
            bis.mark(1024);

            String start = new String(BUFFER, 0, bis.read(BUFFER));
            if (start.equals("materializer locked")) return null;

            bis.reset();

            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(bis);
        }
    }
}
