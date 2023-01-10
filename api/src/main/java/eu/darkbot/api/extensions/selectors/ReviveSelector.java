package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.enums.ReviveLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Provider of a supplier to use when deciding where to revive
 * @see PrioritizedSupplier
 */
public interface ReviveSelector {

    @NotNull PrioritizedSupplier<ReviveLocation> getReviveLocationSupplier();
}
