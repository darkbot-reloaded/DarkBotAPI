package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for any Npc event
 */
public interface NpcEventAPI extends API.Singleton {

    /**
     * Get how long until the current {@link #getStatus()} changes.
     * @return how long until the npc is active/inactive, in seconds
     */
    double getRemainingTime();

    /**
     * @return the current status of the Npc event
     */
    Status getStatus();

    enum Status {
        ACTIVE, INACTIVE, ENDED
    }

}
