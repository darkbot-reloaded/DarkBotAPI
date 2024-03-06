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
     * @return The {@code List} of all available gates
     */
    List<? extends Gate> getAvailableGates();

    /**
     * In game retriever representation, includes name, in game name, type, duration &amp; slot id
     */
    interface Retriever {
        /**
         * @return retriever Id
         */
        int getId();

        /**
         * @return in game variable name
         */
        String getIconId();

        /**
         * @return short game name
         */
        String getName();

        /**
         * @return retriever description id
         */
        String getDescriptionId();

        /**
         * @return the time to build
         */

        double getDuration();

        /**
         * @return slot position of the retriever that are in progress
         */
        int getSlotId();

        /**
         * @return tier of retriever
         */
        int getTier();

        /**
         * @return if retriever is available
         */
        boolean isAvailable();

        /**
         * @return cost list for the retriever
         */
        List<? extends Cost> getCostList();

        /**
         * @return instant cost for the retriever
         */
        Cost getInstantCost();
    }

    /**
     * In game gate representation, includes name, in game name, type, duration &amp; slot id
     */
    interface Gate {
        /**
         * @return status
         */
        int getStatus();

        /**
         * @return gate Id
         */
        int getGateId();

        /**
         * @return the time to build
         */
        int getDuration();

        /**
         * @return the time left to complete
         */
        double getTimeLeft();

        /**
         * @return gate dispatch id
         */
        String getDispatchId();

        /**
         * @return in game variable name
         */
        String getIconId();

        /**
         * @return game name
         */
        String getName();

        /**
         * @return cost for the gate
         */
        Cost getCost();
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

    /**
     * @param retriever to override selected address to
     */
    void overrideSelectedRetriever(Retriever retriever);

}
