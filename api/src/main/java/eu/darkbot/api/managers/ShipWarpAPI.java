package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Ship Warp data
 */
public interface ShipWarpAPI extends API.Singleton {

    /**
     * Open's ship warp window to update proxy data
     * @return true if done opening, false if more calls are required
     */
    boolean updateShipList();

    /**
     * @return if Ship is near a station to warp for free
     */
    boolean isNearSpaceStation();

    /**
     * @return The {@code List} of all warp able ship that are favorite
     */
    List<? extends Ship> getShips();

    /**
     * Works towards selecting a ship for warp, may require multiple calls
     * @param ship The ship to select for warping. Must be an instance obtained from {@link #getShips()}
     * @return true if done, false if failed or more calls are required
     */
    boolean clickShip(Ship ship);

    /**
     * Works towards selecting a ship for warp, may require multiple calls
     * @param index The index in {@link #getShips()} to warp to. Out of bounds return false.
     * @return true if done, false if failed or more calls are required
     */
    boolean clickShip(int index);

    /**
     * Works towards warping to the selected ship, may require multiple calls
     * @return true if done, false if more calls are required
     */
    boolean clickWarp();

    /**
     * In Game Ship representation to warp to
     */
    interface Ship {
        /**
         * @return ship id, this value is unique
         */
        int getShipId();

        /**
         * @return the cost to warp at current location
         */
        int getWarpCost();

        /**
         * @return if warping is free at current location
         */
        boolean isFreeWarp();

        /**
         * @return the index of favorite order
         */
        int getFavouriteIndex();

        /**
         * @return the mapId of where ship is located
         */
        int getMapId();

        /**
         * @return the ship type id
         */
        String getShipTypeId();

        /**
         * @return the full ship name
         */
        String getShipName();
    }
}
