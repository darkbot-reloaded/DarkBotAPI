package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for any Npc event
 */
public interface DefaultNpcEventAPI extends API.Singleton {

    /**
     * Get how long until the current {@link #getStatus()} changes.
     *
     * @return how long until the npc is active/inactive, in seconds
     */
    double getRemainingTime();

    /**
     * @return the current npc name
     */
    String getNpcName();

    /**
     * @return the current event id
     */
    String getEventId();

    /**
     * @return the message description during the event
     */
    String getEventDescription();

    /**
     * @return the message for npc's to kill
     */
    String getRemainingNpcsDescription();

    /**
     * @return the number of npc left on the map
     */
    int getRemainingNpcCount();

    /**
     * @return the number of boss npc left on the map
     */
    int getBossCount();

    /**
     * @return the current status of the Npc event
     */
    Status getStatus();

    enum Status {
        ACTIVE, INACTIVE
    }

}
