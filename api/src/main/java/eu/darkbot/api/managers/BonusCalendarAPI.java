package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

public interface BonusCalendarAPI extends API.Singleton {
    /**
     * @return number of days claimed
     */
    int getDaysClaimed();

    /**
     * @return The {@code List} of all rewards from rewards list
     */
    List<? extends RewardList> getRewardList();

    /**
     * @return if reward is claimable for the day
     */
    boolean isClaimable();


    /**
     * In game rewards loot representation, includes loot id, &amp; amount
     */
    interface RewardList {

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
