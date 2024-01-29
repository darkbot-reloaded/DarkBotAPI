package eu.darkbot.api.game.items;

import eu.darkbot.api.managers.HeroItemsAPI;

import java.util.Locale;

/**
 * Represents all available categories of {@link HeroItemsAPI} items.
 */
public enum ItemCategory {
    LASERS,
    ROCKETS,
    ROCKET_LAUNCHERS,
    SPECIAL_ITEMS,
    MINES,
    CPUS,
    BUY_NOW,
    TECH_ITEMS,
    SHIP_ABILITIES,
    DRONE_FORMATIONS,
    PET;

    private static final ItemCategory[] VALUES = values();

    public static ItemCategory of(String categoryId) {
        for (ItemCategory itemCategory : VALUES) {
            if (itemCategory.getId().equals(categoryId))
                return itemCategory;
        }

        return null;
    }

    public String getId() {
        return name().toLowerCase(Locale.ROOT);
    }
}
