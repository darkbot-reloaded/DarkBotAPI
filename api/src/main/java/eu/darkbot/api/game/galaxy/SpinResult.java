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

    SpinInfo getMines();
    SpinInfo getParts();
    SpinInfo getRockets();
    SpinInfo getXenomit();
    SpinInfo getNanoHull();
    SpinInfo getLogFiles();
    SpinInfo getVouchers();
    SpinInfo getMultipliers();

    Map<SelectableItem.Laser, SpinInfo> getAmmo();

    interface SpinInfo {
        int getObtained();
        int getSpinsUsed();
    }
}
