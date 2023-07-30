package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provides access to data for the Astral galaxy gate in-game.
 */
public interface AstralGateAPI extends API.Singleton {
    /**
     * Making the gate appear on the map requires the use of one CPU (acts as a
     * key).
     *
     * @return Amount of CPUs available.
     */
    int getCpuCount();

    /**
     * @return The current score for the rank
     */
    int getCurrentScore();

    /**
     * @return The current rift in the gate
     */
    int getCurrentRift();

    /**
     * @return The high score
     */
    int getHighScore();

    /**
     * @return If has access to the equipment management tab
     */
    boolean allowedToEquip();

    /**
     * The list of items available to use the ship inside the GG.
     *
     * @return The list of all inventory items
     */
    List<? extends AstralItem> getInventoryItems();

    /**
     * The list of items options to pick from.
     * Once picked they will get added up to your inventory items.
     *
     * @see #getInventoryItems()
     *
     * @return The list of available items to pick
     */
    List<? extends AstralItem> getRewardsItems();

    /**
     * Item inside the GG
     */
    interface AstralItem {
        /**
         * @return If the item is equipped on the ship
         */
        boolean isEquipped();

        /**
         * @return The upgrade level of the item
         */
        int getUpgradeLevel();

        /**
         * @return The Loot Id in the game
         */
        String getLootId();

        /**
         * @return List of the item's stats, if available.
         */
        List<? extends ItemStat> getStats();
    }

    /**
     * Item Stat inside the GG
     */
    interface ItemStat {
        /**
         * @return The stat attribute name
         */
        String getAttribute();

        /**
         * @return The stat attribute value
         */
        double getValue();
    }
}
