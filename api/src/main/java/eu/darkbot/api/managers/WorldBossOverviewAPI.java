package eu.darkbot.api.managers;

import eu.darkbot.api.API;

/**
 * Provides data for the World Boss Treacherous Domain event
 */
public interface WorldBossOverviewAPI extends API.Singleton {
    /**
     * @return the current amount of attempts left for World Boss Treacherous Domain Event
     */
    int getAttempts();

    /**
     * @return the current tier of Boss in World Boss Treacherous Domain Event
     */
    int getCurrentTier();

    Status getStatus();

    String getBossName();

    String getBannerKey();


    enum Status {
        AVAILABLE, COMPLETED
    }

}
