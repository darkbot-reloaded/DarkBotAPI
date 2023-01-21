package eu.darkbot.api.game.items;

import eu.darkbot.api.managers.HeroItemsAPI;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;

/**
 * Represents an item in-game, the type of this item is a {@link SelectableItem}
 */
public interface Item extends SelectableItem {

    /**
     * @return id of the {@link Item}
     */
    @Override
    String getId();

    /**
     * Get the constant representation of this item, if it exists
     *
     * @return item constant, if it exists, null otherwise
     */
    <T extends Enum<T> & SelectableItem> @Nullable T getAs(Class<T> type);

    /**
     * Checks if item can be used in-game &amp; by the API, you should check quantity.
     */
    default boolean isReadyToUse() {
        return isReady() && isActivatable() && isUsable();
    }

    /**
     * @return current quantity of item
     */
    double getQuantity();

    /**
     * Checks if item can be used by current API {@link HeroItemsAPI#useItem(SelectableItem, ItemFlag...)}
     * Doesn't check cooldown etc.
     *
     * @return true if item can be used in-game and by API, may be in timer
     */
    boolean isUsable();

    /**
     * @return true if item is selected in-game
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
     * Checks if item can be activated:
     * isn't in cooldown, not restricted by current game map, etc.
     * <p>
     * Keep in mind that item may be activated but API may not be able to use this item {@link #isUsable()}
     *
     * @return true if item can be activated in-game
     */
    boolean isActivatable();

    /**
     * @return last successful use attempt time of {@link Item} in milliseconds from epoch, 0 = no attempts
     */
    long lastUseTime();

    /**
     * @return true if item is ready - not cooling down/not activated for period of time
     */
    default boolean isReady() {
        return getTimer() == null;
    }

    /**
     * Will return the {@link ItemTimer} if item is cooling down or is activated for X time in-game
     *
     * @return {@link ItemTimer} if item is activated/cooling down, null otherwise
     */
    @UnknownNullability ItemTimer getTimer();

    /**
     * Will optionally get the {@link ItemTimer} if item is cooling down or is activated for X time in-game
     *
     * @return ItemTimer if present, {@link Optional#empty()} otherwise
     */
    @Deprecated
    Optional<ItemTimer> getItemTimer();
}
