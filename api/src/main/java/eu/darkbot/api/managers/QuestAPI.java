package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Provides access to data for the Quests in-game.
 */
public interface QuestAPI extends API.Singleton {

    /**
     * @return The quest that is currently active and in view.
     *         Even if there are several accepted quests, only the selected one is
     *         returned.
     */
    @Nullable Quest getCurrentQuestDisplayed();

    /**
     * @return The quest that is currently selected in the QuestGiver
     */
    @Nullable Quest getCurrentQuestSeleted();

    /**
     * @return Returns a list of current quests, only has data when QuestGiver is
     *         opened
     */
    @Nullable List<QuestListItem> getCurrestQuests();

    /**
     * Quest Object
     */
    interface Quest {

        /**
         * @return The Quest ID
         */
        int getId();

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

        /**
         * @return True if completed
         */
        boolean isCompleted();

        /**
         * @return List of conditions for completion
         */
        List<Contition> getConditions();

        /**
         * @return List of rewards to be obtained by completing the quest
         */
        List<Reward> getRewards();
    }

    /**
     * QuestListItem Object
     */
    interface QuestListItem {

        /**
         * @return The Quest ID
         */
        int getId();

        /**
         * @return Level required to activate it
         */
        int getLevelRequired();

        /**
         * @return True if selected in the interface
         */
        boolean isSelected();

        /**
         * @return True if completed
         */
        boolean isCompleted();

        /**
         * @return Quest Title
         */
        String getTitle();

        /**
         * @return Type of quest
         */
        String getType();

        /**
         * @return True if it can be activated
         */
        boolean isActivable();
    }

    /**
     * Contition Object
     */
    interface Contition {

        /**
         * @return Description of the condition
         */
        String getDescription();

        /**
         * @return Completed amount of the condition
         */
        double getGoalReached();

        /**
         * @return Amount needed to complete the condition
         */
        double getGoal();

        /**
         * @return True if the condition has been completed
         */
        boolean isCompleted();

        /**
         * @return Type of condition
         */
        String getConditionType();

        /**
         * @return Returns the list of conditions that the condition itself may have
         */
        List<Contition> getConditions();
    }

    /**
     * Reward Object
     */
    interface Reward {

        /**
         * @return Amount to be obtained
         */
        int getAmount();

        /**
         * @return Type to be obtained
         */
        String getType();
    }
}
