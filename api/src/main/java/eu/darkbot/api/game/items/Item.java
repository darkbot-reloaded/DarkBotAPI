package eu.darkbot.api.game.items;

import java.util.Optional;

/**
 * Represents an item in-game, the type of this item is a {@link SelectableItem}
 */
public interface Item extends SelectableItem {

    /**
     * @return id of the {@link Item}
     */
    String getId();

    /**
     * @return optional constant representation of this item if present
     */
    <T extends Enum<T> & SelectableItem> Optional<T> getAs(Class<T> type);

    /**
     * @return current quantity of item
     */
    double getQuantity();

    /**
     * @return true if item can be used in-game and by API
     */
    boolean isUsable();

    /**
     * @return true if item is selected
     */
    boolean isSelected();

    /**
     * @return true if item can be bought via selection
     */
    boolean isBuyable();

    /**
     * @return true if item is available and is not greyed out
     */
    boolean isAvailable();

    /**
     * @return last successful use attempt time of {@link Item} in milliseconds from epoch, 0 = no attempts
     */
    long lastUseTime();

    /**
     * @return true if item is ready - not cooling down
     */
    default boolean isReady() {
        return !getItemTimer().isPresent();
    }

    /**
     * Will optionally get the {@link ItemTimer} if item is cooling down or is activated for X time in-game
     *
     * @return ItemTimer if present, {@link Optional#empty()} otherwise
     */
    Optional<ItemTimer> getItemTimer();
}
