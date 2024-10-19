package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provides Inventory Data
 */

public interface InventoryAPI extends API.Singleton {
    /**
     * @return list of items in inventory
     */
    List<? extends Item> getItems(int minWaitMs);


    interface Item {
        /**
         * @return item loot id
         */
        String getLootId();

        /**
         * @return a readable anme
         */
        String getName();

        /**
         * @return amount of item
         */
        double getAmount();
    }
}
