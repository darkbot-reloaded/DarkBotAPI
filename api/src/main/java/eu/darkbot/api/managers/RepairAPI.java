package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.game.enums.ReviveLocation;
import eu.darkbot.api.game.other.Locatable;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Provides access to repair options, checking amount of deaths, or get what destroyed you.
 */
public interface RepairAPI extends API.Singleton {

    /**
     * Change death statistics to 0
     */
    void resetDeaths();

    /**
     * @return The amount of times you have been killed
     */
    int getDeathAmount();

    /**
     * @return if the hero is currently destroyed
     */
    boolean isDestroyed();

    /**
     * @param reviveLocation revive location to check
     * @return time in seconds until becomes available, 0 - ready, -1 - not available at all
     */
    int isAvailableIn(ReviveLocation reviveLocation);

    default boolean isAvailable(ReviveLocation reviveLocation) {
        return isAvailableIn(reviveLocation) != -1;
    }

    default boolean isReady(ReviveLocation reviveLocation) {
        return isAvailableIn(reviveLocation) == 0;
    }

    /**
     * @return The name of the last thing that destroyed you.
     * This could be a player name or something else, eg: Mine, or an NPC name.
     */
    @Nullable String getLastDestroyerName();

    /**
     * @return Instant when the hero last died
     */
    @Nullable Instant getLastDeathTime();

    /**
     * @return The location where the hero last died
     */
    @Nullable Locatable getLastDeathLocation();
}
