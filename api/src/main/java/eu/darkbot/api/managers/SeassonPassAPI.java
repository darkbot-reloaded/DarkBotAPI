package eu.darkbot.api.managers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import eu.darkbot.api.API;
import eu.darkbot.api.managers.QuestAPI.Quest;

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
        boolean goldLocked();

        boolean oncePreMission();

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

        enum QuestStatus {
            UNKNOWN(-1),
            COMPLETED(0),
            GOLD_LOCKED(2),
            NOT_COMPLETED(3);

            private final int value;

            QuestStatus(int value) {
                this.value = value;
            }

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
