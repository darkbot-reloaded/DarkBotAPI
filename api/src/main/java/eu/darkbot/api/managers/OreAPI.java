package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.game.entities.Station;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Locale;

/**
 * Provides access to introspect &amp; interact with ores, like checking current amount or selling.
 */
public interface OreAPI extends API.Singleton {

    /**
     * @param ore or to check
     * @return amount of owned {@link Ore}
     */
    int getAmount(@NotNull Ore ore);

    /**
     * Sells the specified ore, trade window must be open for this method to work
     *
     * @param ore the {@code Ore} you want to sell it must be {@code sellable}
     * @see #showTrade
     * @see #canSellOres
     * @see Ore#sellable
     */
    void sellOre(@NotNull Ore ore);

    /**
     * Determines if ores can be sold based on if the ore trade window is open or not.
     *
     * @return true if ore trade window is open, false otherwise
     */
    boolean canSellOres();

    /**
     * Will either open or close the ore trade window based on the value of {@code show}
     *
     * @param show       true for showing ore trade window, false for closing ore trade window
     * @param tradePoint the {@code BasePoint} of the ore trader base station. For show(false) this is ignored.
     * @return true if ore trader window has been opened or closed and its animation is done,
     * false if animation is not done, or no action is needed to be taken
     * to change the visibility status of the ore trader window
     */
    boolean showTrade(boolean show, @Nullable("if show == false") Station.Refinery tradePoint);

    /**
     * Types of Ores visible in refinery window
     */
    enum Ore {
        PROMETIUM(0),
        ENDRIUM(1),
        TERBIUM(2),
        XENOMIT(3, false),
        PROMETID(4),
        DURANIUM(5),
        PROMERIUM(6),
        SEPROM(7),
        PALLADIUM(8),
        OSMIUM(28);

        private final int id;
        private final boolean sellable;

        Ore(int id) {
            this(id, true);
        }

        Ore(int id, boolean sellable) {
            this.id = id;
            this.sellable = sellable;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public boolean isSellable() {
            return sellable;
        }
    }
}
