package eu.darkbot.api.managers;

import java.util.List;

/**
 * Provides Inventory Data
 */

public interface InventoryAPI {
    /**
     * @return list of items in inventory
     */
    List<? extends Item> getItems();


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
