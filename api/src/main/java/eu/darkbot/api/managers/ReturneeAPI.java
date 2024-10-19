package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Returnee data
 */
public interface ReturneeAPI extends API.Singleton {

    /**
     * @return if one time login reward is claimable
     */
    boolean isLoginClaimable();

    /**
     * @return {@code List} of all Rewards for one time login claimable
     */
    List<? extends LoginRewardList> getLoginRewardList();

    /**
     * Provide access to one time Reward data in Retunee window containing loot id and amount of the item
     */
    interface LoginRewardList {
        String getLootId();

        double getAmount();
    }

    /**
     * @return if daily calendar reward is claimable
     */
    boolean isCalendarClaimable();

    /**
     * @return {@code List} of all Daily calendar rewards
     */
    List<? extends CalendarRewardList> getCalendarRewardList();

    /**
     * Provide access to daily  calendar reward data in Retunee window containing
     *      loot id and amount of the item, and if claimed
     */
    interface CalendarRewardList {
        String getLootId();

        int getAmount();

        boolean getClaimed();
    }
}
