package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GalaxyInfo;
import eu.darkbot.api.game.galaxy.GateInfo;
import eu.darkbot.api.game.galaxy.SpinResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Provides access to retrieve Galaxy Gate info, as well as build &amp; place them.
 */
public interface GalaxySpinnerAPI extends API.Singleton {
    /**
     * @return the {@link GalaxyInfo} with all infos about current state of spinner
     */
    @NotNull GalaxyInfo getGalaxyInfo();

    /**
     * @param expiryTime only update if within
     * @return null if update wasn't required (non-expired), true if updated ok, false if update failed
     */
    @Nullable Boolean updateGalaxyInfos(int expiryTime);

    /**
     * Spins gg energy into a gate
     *
     * @param gate       The gate to spin
     * @param useMultiAt Minimum amount to use multiplier, 2, 3, 4, 5 or 6
     * @param spinAmount Amount to spin, 1, 5, 10 or 100
     * @param minWait    Minimum time(ms) to wait since any last backpage request
     * @return non-empty optional of {@link SpinResult} if request was filled successfully
     */
    default Optional<SpinResult> spinGate(@NotNull GalaxyGate gate, int useMultiAt, int spinAmount, int minWait) {
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
     * @param minWait    Minimum time(ms) to wait since any last backpage request
     * @return non-empty optional of {@link SpinResult} if request was filled successfully
     */
    Optional<SpinResult> spinGate(@NotNull GalaxyGate gate, boolean multiplier, int spinAmount, int minWait);

    /**
     * @return overall amount of possible successful in-game spins - value may not be reliable
     * @see #spinGate - spinAmount parameter
     */
    int getSpinsUsed();

    /**
     * Place the gate on your x-1 map
     *
     * @param gate    The gate to place
     * @param minWait Minimum time(ms) to wait since any last backpage request
     * @return if the request was filled successfully
     * @see GateInfo#canBePlaced()
     */
    boolean placeGate(@NotNull GalaxyGate gate, int minWait);

    /**
     * @param gate    The gate to buy life
     * @param minWait Minimum time(ms) to wait since any last backpage request
     * @return if the request was filled successfully
     */
    boolean buyLife(@NotNull GalaxyGate gate, int minWait);

    class SpinGateEvent implements Event {
        private final SpinResult spinResult;
        private final int spinAmount;

        public SpinGateEvent(SpinResult spinResult, int spinAmount) {
            this.spinResult = spinResult;
            this.spinAmount = spinAmount;
        }

        public SpinResult getSpinResult() {
            return spinResult;
        }

        public int getSpinAmount() {
            return spinAmount;
        }
    }

    class PlaceGateEvent implements Event {
        private final GalaxyGate gate;

        public PlaceGateEvent(GalaxyGate gate) {
            this.gate = gate;
        }

        public GalaxyGate getGate() {
            return gate;
        }
    }
}
