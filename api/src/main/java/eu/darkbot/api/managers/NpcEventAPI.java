package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for any Npc event
 */
public interface NpcEventAPI extends API.Singleton {

    NpcEvent getEvent(EventType type);

    enum EventType {
        GENERIC,
        AGATUS
    }

    interface NpcEvent {

        /**
         * Get how long until the current {@link #getStatus()} changes.
         *
         * @return how long until the npc is active/inactive, in seconds
         */
        double getRemainingTime();

        /**
         * @return the current event name
         */
        String getEventName();

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
        String getNpcLeftDescription();

        /**
         * @return the number of npc left on the map
         */
        int npcLeft();

        /**
         * @return the number of boss npc left on the map
         */
        int bossNpcLeft();

        /**
         * @return the current status of the Npc event
         */
        Status getStatus();

        enum Status {
            ACTIVE, INACTIVE
        }
    }
}
