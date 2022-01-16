package eu.darkbot.api.game.entities;

import eu.darkbot.api.game.other.Attacker;
import eu.darkbot.api.game.other.Movable;

/**
 * In-game generic ship on the map, like players, npc, pets and more.
 */
public interface Ship extends Attacker, Movable {
    /**
     * @return id of the ship type in-game
     */
    int getShipId();

    /**
     * @return true if ship is invisible/cloaked.
     */
    boolean isInvisible();

    /**
     * If this ship has been blacklisted by calling {@link #setBlacklisted}
     *
     * @return if this {@link Ship} is currently blacklisted, false if blacklist has expired
     */
    boolean isBlacklisted();

    /**
     * Adds this {@link Ship} to blacklist for given time (ms).
     * The main use-case is remembering this enemy ship attacked you, but can be used
     * for other purposes like NPCs that are bugged or have been attacked by others.
     * <p>
     * By itself, this changes nothing but the response of {@link #isBlacklisted}
     *
     * @param time time in milliseconds to stay in the blacklist
     */
    void setBlacklisted(long time);
}
