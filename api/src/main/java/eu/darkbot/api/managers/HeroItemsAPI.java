package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.game.items.Item;
import eu.darkbot.api.game.items.ItemCategory;
import eu.darkbot.api.game.items.ItemFlag;
import eu.darkbot.api.game.items.ItemUseResult;
import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * API to manage in-game items, from ammo, to rockets, abilities or even fireworks.
 *
 * @see SelectableItem
 */
public interface HeroItemsAPI extends API.Singleton {
    /**
     * @param itemCategory the category to get items from
     * @return a Collection with all the {@link Item}s inside the given {@link ItemCategory}
     */
    Collection<? extends Item> getItems(@NotNull ItemCategory itemCategory);

    /**
     * Get {@link Item} representation of given {@link SelectableItem} if exists and matches every {@link ItemFlag} passed.
     *
     * @param selectableItem item to get representation of
     * @param itemFlags      optional flags which this method must respect
     * @return {@link Item} representation of given {@link SelectableItem} if exists &amp; matches given flags, otherwise {@link Optional#empty()}
     */
    Optional<Item> getItem(@NotNull SelectableItem selectableItem, ItemFlag... itemFlags);

    /**
     * Will try to use given {@link SelectableItem} with optional additional {@link ItemFlag}s.
     * <p>
     * By default this method uses {@link ItemFlag#AVAILABLE},
     * {@link ItemFlag#READY} &amp; {@link ItemFlag#USABLE} flags which cannot be omitted.
     * <p>
     * You can pass own flag set which be checked with defaults together.
     * </p>
     *
     * @param selectableItem item to be used
     * @param itemFlags      optional flags which this method must respect
     * @return use result of the selectableItem
     */
    ItemUseResult useItem(@NotNull SelectableItem selectableItem, ItemFlag... itemFlags);

    /**
     * Will try to use given {@link SelectableItem} with optional additional {@link ItemFlag}s
     * and has not been used within specified minWait time.
     * <p>
     * By default this method uses {@link ItemFlag#AVAILABLE},
     * {@link ItemFlag#READY} &amp; {@link ItemFlag#USABLE} flags which cannot be omitted.
     * <p>
     * You can pass own flag set which be checked with defaults together.
     * </p>
     *
     * @param selectableItem item to be used
     * @param minWait        minimum wait time(ms) between last successful use attempt
     * @param itemFlags      optional flags which this method must respect
     * @return use result of the selectableItem
     */
    ItemUseResult useItem(@NotNull SelectableItem selectableItem, double minWait, ItemFlag... itemFlags);
}
