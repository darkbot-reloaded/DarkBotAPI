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
     * @return available dispatch slots that can be used
     */
    int getAvailableSlots();

    /**
     * @return total slots that can be used
     */
    int getTotalSlots();

    /**
     * @return The {@code List} of all available retrievers
     */
    List<? extends Retriever> getAvailableRetrievers();

    /**
     * @return The {@code List} of all in progress retrievers
     */
    List<? extends Retriever> getInProgressRetrievers();

    /**
     * @return The {@code Retriever} of the selected retriever
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
         * @return retriever description id
         */
        String getDescriptionId();

        /**
         * @return the time to build or complete retriever
         */

        double getDuration();

        /**
         * @return slot position of the retriever that are in progress
         */
        int getSlotId();
    }

    /**
     * In game representation of cost of retriever
     */
    interface Cost {
        /**
         * @return in game loot name
         */
        String getLootId();

        /**
         * @return amount of required resource for retriever
         */
        int getAmount();

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
