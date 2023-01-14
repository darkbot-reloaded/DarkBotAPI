package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for the mimesis mutinity event, escort gate in-game
 */
public interface GauntletPlutusAPI extends API.Singleton {
    /**
     * @return the current status of the Gauntlet of Plutus
     */
    Status getStatus();

    enum Status {
        INSIDE, AVAILABLE, COMPLETED, ENDED
    }

}
