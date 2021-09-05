package eu.darkbot.api.config.types;

import org.jetbrains.annotations.Contract;

import java.awt.*;

/**
 * A tag that can be added or removed to players
 */
public interface PlayerTag {

    /**
     * @return name assigned to the tag by the user
     */
    String getName();

    /**
     * @return color assigned to the tag by the user
     */
    Color getColor();

    /**
     * Check if a certain player has the tag
     * @see PlayerInfo#getTags()
     * @see PlayerInfo.PlayerTags#contains(PlayerTag)
     *
     * @param info the player info to check
     * @return true if the player has the tag, false otherwise
     */
    @Contract("null -> false")
    default boolean hasTag(PlayerInfo info) {
        return info != null && info.getTags().contains(this);
    }

}
