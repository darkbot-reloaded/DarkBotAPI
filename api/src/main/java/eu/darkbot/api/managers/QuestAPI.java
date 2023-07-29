package eu.darkbot.api.managers;

import org.jetbrains.annotations.Nullable;

import eu.darkbot.api.API;

/**
 * Provides access to data for the Quests in-game.
 */
public interface QuestAPI extends API.Singleton {

    /**
     * @return The quest that is currently active and in view.
     *         Even if there are several accepted quests, only the selected one is
     *         returned.
     */
    @Nullable Quest getCurrenQuest();

    /**
     * Quest Object
     */
    interface Quest {

        /**
         * @return The Quest ID
         */
        int getCurrentQuestId();

        /**
         * @return If the quest is Active
         */
        boolean isActive();

        /**
         * @return The quest category
         */
        String getCategory();

        /**
         * @return The title of the quest in the client's language
         */
        String getTitle();

        /**
         * @return The description of the quest in the client's language
         */
        String getDescription();

        /**
         * @return The number of conditions the quest has to be completed.
         */
        int getConditionsCount();
    }
}
