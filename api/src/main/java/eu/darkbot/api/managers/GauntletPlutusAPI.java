package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for the Gauntlet of Plutus event
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
