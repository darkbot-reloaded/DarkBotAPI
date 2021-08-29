package eu.darkbot.api.game.galaxy;

import eu.darkbot.api.game.items.SelectableItem;

import java.time.Instant;
import java.util.Map;

public interface SpinResult {
    /**
     * @return the {@link Instant} when that spin was fulfilled
     */
    Instant getDate();

    /**
     * @return {@link GalaxyGate} on which spin was used
     */
    GalaxyGate getGalaxyGate();

    /**
     * @return amount of parts obtained for GalaxyGate
     * @see #getGalaxyGate()
     */
    int getParts();

    /**
     * Multipliers obtained via spin.
     * Amount of multipliers obtained equals amount of duplicate parts obtained.
     *
     * @return amount of multipliers obtained
     */
    int getMultipliers();

    SpinInfo getMines();
    SpinInfo getRockets();
    SpinInfo getXenomit();
    SpinInfo getNanoHull();
    SpinInfo getLogFiles();
    SpinInfo getVouchers();

    Map<SelectableItem.Laser, SpinInfo> getAmmo();

    interface SpinInfo {
        int getObtained();
        int getSpinsUsed();
    }
}
