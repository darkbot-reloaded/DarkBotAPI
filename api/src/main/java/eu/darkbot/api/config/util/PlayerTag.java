package eu.darkbot.api.config.util;

import java.awt.*;

/**
 * A tag that can be added or removed to players
 *
 *
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

}
