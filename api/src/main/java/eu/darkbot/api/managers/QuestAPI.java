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
    @Nullable Quest getDisplayedQuest();

    /**
     * @return The quest that is currently selected in the QuestGiver
     */
    @Nullable Quest getSelectedQuest();

    /**
     * @return Returns additional information on the selected quest
     */
    @Nullable QuestListItem getSelectedQuestInfo();

    /**
     * @return Returns a list of current quests, only has data when QuestGiver is
     *         opened
     */
    @Nullable List<QuestListItem> getCurrestQuests();

    /**
     * @return Returns true if the quest giver is open
     */
    boolean isVisibleQuestGiver();

    /**
     * @return Returns the tab position that is being displayed in the QuestGiver
     */
    int getSelectedTab();

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
         * @return The number of requirements the quest has to be completed.
         */
        int getRequirementsCount();

        /**
         * @return True if completed
         */
        boolean isCompleted();

        /**
         * @return List of requirements for completion
         */
        List<Requirement> getRequirements();

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
     * Requirement Object
     */
    interface Requirement {

        /**
         * @return Description of the requirement
         */
        String getDescription();

        /**
         * @return Completed amount of the requirement
         */
        double getGoalReached();

        /**
         * @return Amount needed to complete the requirement
         */
        double getGoal();

        /**
         * @return True if the requirement has been completed
         */
        boolean isCompleted();

        /**
         * @return Type of requirement
         */
        String getRequirementType();

        /**
         * @return Returns the list of requirements that the requirement itself may have
         */
        List<Requirement> getRequirements();

        /**
         * @return Returns whether the requirements can be realised or not.
         */
        boolean isEnabled();
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
