package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for the mimesis mutinity event, escort gate in-game
 */
public interface EscortAPI extends API.Singleton {
    /**
     * Time displayed in-game in the escort window, can mean different things, like:
     * <br>
     *  - Time until the gate is open (lets players join), could be hours if the gate is closed until the next day
     * <br>
     *  - Time until the gate starts (waiting for players to join) usually 5 minutes
     *
     * @return Time left until next change, in seconds.
     */
    double getTime();

    /**
     * Current amount of keys, one is required to enter the gate.
     *
     * @return current amount of keys
     */
    double getKeys();
}
