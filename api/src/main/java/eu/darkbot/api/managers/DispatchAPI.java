package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Dispatch data
 */
public interface DispatchAPI extends API.Singleton {

    /**
     * @return The {@code List} of all rewards from retriever in last collection
     */
    List<? extends RewardLoot> getRewardLoot();

    /**
     * In game rewards loot representation, includes loot id, &amp; amount
     */
    interface RewardLoot {
        /**
         * @return in game variable name of reward
         */

        String getLootId();
        /**
         * @return amount of reward
         */
        int getAmount();

    }

}
