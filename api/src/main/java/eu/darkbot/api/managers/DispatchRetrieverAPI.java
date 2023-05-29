package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Dispatch Retriever data
 */
public interface DispatchRetrieverAPI extends API.Singleton {

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
    List<? extends DispatchRetrieverVO> getAvailableRetrievers();

    /**
     * @return The {@code List} of all in progress retrivers
     */
    List<? extends DispatchRetrieverVO> getInProgressRetrievers();

    /**
     * @return The {@code DispatchRetrieverVO} of the selected retriever
     */
    DispatchRetrieverVO getSelectedRetriever();

    /**
     * In game retrievers representation, includes name, in game name, type, duration &amp; slot id
     */
    interface DispatchRetrieverVO {
        /**
         * @return short game name
         */
        String getName();

        /**
         * @return in game variable name
         */

        String getInGameName();

        /**
         * @return retriever type
         */
        String getType();

        /**
         * @return the time to build or complete retriever
         */

        double getDuration();

        /**
         * @return slot position of the retriever
         */
        int getSlotId();
    }

}
