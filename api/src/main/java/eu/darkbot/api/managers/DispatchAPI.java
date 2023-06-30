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
     * @return avaialbe dispatch slots  to be used
     */
    int getAvailableSlots();

    /**
     * @return total slots that can be used
     */
    int getTotalSlots();

    /**
     * @return The {@code List} of all available retrivers
     */
    List<? extends Retriever> getAvailableRetrievers();

    /**
     * @return The {@code List} of all in progress retrivers
     */
    List<? extends Retriever> getInProgressRetrievers();

    /**
     * @return The {@code DispatchRetrieverVO} of the selected retriever
     */
    Retriever getSelectedRetriever();

    /**
     * In game retriever representation, includes name, in game name, type, duration &amp; slot id
     */
    interface Retriever {
        /**
         * @return in game variable name
         */

        String getId();

        /**
         * @return short game name
         */
        String getName();

        /**
         * @return retriever type
         */
        String getDescription();

        /**
         * @return the time to build or complete retriever
         */

        double getDuration();

        /**
         * @return slot position of the retriever
         */
        int getSlotId();
    }

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
