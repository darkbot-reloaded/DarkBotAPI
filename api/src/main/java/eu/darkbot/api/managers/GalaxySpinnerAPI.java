package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GalaxyInfo;
import eu.darkbot.api.game.galaxy.GateInfo;
import org.jetbrains.annotations.Nullable;

public interface GalaxySpinnerAPI extends API.Singleton {
    /**
     * @return the {@link GalaxyInfo} with all infos about current state of spinner
     */
    GalaxyInfo getGalaxyInfo();

    /**
     * Spins gg energy into a gate
     *
     * @param gate       The gate to spin
     * @param useMultiAt Minimum amount to use multiplier, 2, 3, 4, 5 or 6
     * @param spinAmount Amount to spin, 1, 5, 10 or 100
     * @param minWait    Minimum time to wait since last request
     * @return if the request was filled successfully
     */
    default boolean spinGate(GalaxyGate gate, int useMultiAt, int spinAmount, int minWait) {
        GateInfo gateInfo = getGalaxyInfo().getGateInfo(gate);
        boolean useMulti = gateInfo != null && gateInfo.getMultiplier() >= useMultiAt;

        return spinGate(gate, useMulti, spinAmount, minWait);
    }

    /**
     * Spins gg energy into a gate
     *
     * @param gate       The gate to spin
     * @param multiplier If multiplier should be used
     * @param spinAmount Amount to spin, 1, 5, 10 or 100
     * @param minWait    Minimum time to wait since last request
     * @return if the request was filled successfully
     */
    boolean spinGate(GalaxyGate gate, boolean multiplier, int spinAmount, int minWait);

    /**
     * Place the gate on your x-1 map
     *
     * @param gate    The gate to place
     * @param minWait Minimum time to wait since last request
     * @return if the request was filled successfully
     */
    boolean placeGate(GalaxyGate gate, int minWait);

    /**
     * @param expiryTime only update if within
     * @return null if update wasn't required (non-expired), true if updated ok, false if update failed
     */
    @Nullable Boolean updateGalaxyInfos(int expiryTime);
}