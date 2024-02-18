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
    @Nullable List<? extends QuestListItem> getCurrestQuests();

    /**
     * @return Returns true if the quest giver is open
     */
    boolean isQuestGiverOpen();

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
        default int getRequirementsCount() {
            return getRequirements().size();
        }

        /**
         * @return True if completed
         */
        boolean isCompleted();

        /**
         * @return List of requirements for completion
         */
        List<? extends Requirement> getRequirements();

        /**
         * @return List of rewards to be obtained by completing the quest
         */
        List<? extends Reward> getRewards();
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
        double getProgress();

        /**
         * @return Amount needed to complete the requirement
         */
        double getGoal();

        /**
         * @return Returns the realised percentage of the requirement
         */
        default double getProgressPercentage() {
            return getProgress() / getGoal();
        }

        /**
         * @return True if the requirement has been completed
         */
        boolean isCompleted();

        /**
         * @return Type of requirement
         */
        default RequirementType getRequirementType() {
            return RequirementType.of(getType());
        }

        /**
         * The string version of requeriment type, prefer using
         * {@link #getRequirementType()} instead
         *
         * @return Type of requirement
         * @see #getRequirementType()
         */
        String getType();

        /**
         * @return Returns the list of requirements that the requirement itself may have
         */
        List<? extends Requirement> getRequirements();

        /**
         * @return Returns whether the requirements can be realised or not.
         */
        boolean isEnabled();

        enum RequirementType {
            UNKNOWN,
            TIMER,
            HASTE,
            ENDURANCE,
            COUNTDOWN,
            COLLECT,
            KILL_NPC,
            DAMAGE,
            AVOID_DAMAGE,
            TAKE_DAMAGE,
            AVOID_DEATH,
            COORDINATES,
            DISTANCE,
            TRAVEL,
            FUEL_SHORTAGE,
            PROXIMITY,
            MAP,
            MAP_DIVERSE,
            EMPTY,
            MISCELLANEOUS,
            AMMUNITION,
            SAVE_AMMUNITION,
            SPEND_AMMUNITION,
            SALVAGE,
            STEAL,
            KILL_NPCS,
            KILL_PLAYERS,
            DAMAGE_NPCS,
            DAMAGE_PLAYERS,
            DAMAGE_ENEMY_PLAYERS,
            VISIT_MAP,
            DIE,
            AVOID_KILL_NPC,
            AVOID_KILL_NPCS,
            AVOID_KILL_PLAYERS,
            AVOID_DAMAGE_NPCS,
            AVOID_DAMAGE_PLAYERS,
            PREVENT,
            JUMP,
            AVOID_JUMP,
            STEADINESS,
            MULTIPLIER,
            STAY_AWAY,
            IN_GROUP,
            KILL_ANY,
            WEB,
            CLIENT,
            CARGO,
            SELL_ORE,
            LEVEL,
            GAIN_INFLUENCE,
            RESTRICT_AMMUNITION_KILL_NPC,
            RESTRICT_AMMUNITION_KILL_PLAYER,
            HONOR_NEGATIVE,
            VISIT_QUEST_GIVER,
            COLLECT_BONUS_BOX,
            QUICK_BUY,
            ENTER_GROUP,
            REAL_TIME_HASTE,
            ACTIVATE_MAP_ASSET_TYPE,
            VISIT_JUMP_GATE_TO_MAP_TYPE,
            VISIT_MULTIPLE_MAPS,
            COLLECT_LOOT,
            FINISH_STARTER_GATE,
            FINISH_GALAXY_GATE,
            FINISH_GALAXY_GATES,
            IN_CLAN,
            REFINE_ORE,
            PUT_ITEM_IN_SLOT_BAR,
            USE_ORE_UPDATE,
            VISIT_MAP_ASSET,
            UPDATE_SKYLAB_TO_LEVEL,
            BEACON_TAKEOVER,
            COLLECT_BUILT_RECIPE,
            VISIT_DISRUPTION_ZONE,
            REAL_TIME_DATE_HASTE,
            COLLECT_BONUS_BOX_TYPE,
            DAMAGE_MAPASSETS,
            COMPLETED_OBJECTIVES,
            ACTIVATE_CPU,
            SPEND_ITEM,
            CLEAR_GALAXY_GATE_WAVE,
            SCORE_ASTRAL_ASCENSION,
            SPEND_RECIPE_INGREDIENTS,
            COMPLETE_SEASON_PASS_QUESTS,
            LOGIN_CONDITION,
            DURING_EVENT;

            private static final RequirementType[] VALUES = values();

            private static RequirementType of(String name) {
                for (RequirementType type : VALUES) {
                    if (type.name().equalsIgnoreCase(name))
                        return type;
                }
                return UNKNOWN;
            }
        }
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
