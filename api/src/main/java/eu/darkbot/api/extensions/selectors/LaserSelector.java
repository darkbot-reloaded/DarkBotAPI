package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.NotNull;

/**
 * Provider of a supplier to use when deciding what ammo to shoot with
 * @see PrioritizedSupplier
 */
public interface LaserSelector {

    @NotNull PrioritizedSupplier<SelectableItem.Laser> getLaserSupplier();
}
