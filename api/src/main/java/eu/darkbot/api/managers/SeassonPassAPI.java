package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.managers.QuestAPI.Quest;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Provides information about the current seasson pass In-game
 */
public interface SeassonPassAPI extends API.Singleton {

    /**
     * @return Returns if a seasson pass is available
     */
    boolean isSeassonPassAvailable();

    /**
     * @return Returns the progress status of the current level
     */
    CurrentLevelProgress getCurrentLevelProgress();

    /**
     * @return Returns the status of the current season's level progress.
     */
    CurrentLevelProgress getCurrentSeassonLevelInfo();

    /**
     * @return Returns a list of the daily seasson quests
     */
    @Nullable List<? extends SeassonPassQuest> getDailyQuests();

    /**
     * @return Returns a list of the weekly seasson quests
     */
    @Nullable List<? extends SeassonPassQuest> getWeeklyQuests();

    /**
     * @return Returns a list of the seasson quests
     */
    @Nullable List<? extends SeassonPassQuest> getSeassonQuests();

    /**
     * @return Returns a list of the all quests
     */
    @Nullable List<? extends SeassonPassQuest> getAllQuests();

    /**
     * CurrentLevelProgress Object
     */
    interface CurrentLevelProgress {
        /**
         * @return Maximum value of progress
         */
        int getMaxProgress();

        /**
         * @return Current value of progress
         */
        int getCurrentProgress();

        /**
         * @return Returns the realised percentage of progress
         */
        double getProgressPercentage();
    }

    /**
     * SeassonPassQuest Object
     */
    interface SeassonPassQuest {
        /**
         * @return Returns if the quest is gold
         */
        boolean isGoldMission();

        /**
         * @return Returns if the quest is blocked
         */
        boolean isGoldLocked();

        /**
         *
         * @return ??
         */
        boolean isOncePreMission();

        /**
         * @return Returns the value of the status
         */
        int getStatus();

        /**
         * @return Return the quest linked to this seasson quest
         */
        @Nullable Quest getQuest();

        /**
         * @return Returns the status of the quest
         */
        default QuestStatus getSeassonPassQuestStatus() {
            return QuestStatus.of(getStatus());
        }

        @AllArgsConstructor
        enum QuestStatus {
            UNKNOWN(-1),
            INCOMPLETE(0),
            INCOMPLETE_UNLOCKED(1),
            INCOMPLETE_LOCKED(2),
            COMPLETED(3),
            COMPLETED_UNLOCKED(4),
            COMPLETED_LOCKED(5);

            private final int value;

            private static final QuestStatus[] VALUES = values();

            private static QuestStatus of(int value) {
                for (QuestStatus type : VALUES) {
                    if (type.value == value)
                        return type;
                }
                return UNKNOWN;
            }
        }

    }
}
