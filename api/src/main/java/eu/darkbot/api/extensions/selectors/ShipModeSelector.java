package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.config.types.ShipMode;
import org.jetbrains.annotations.NotNull;

/**
 * Provider of a supplier to use when deciding what ship mode to use
 * @see PrioritizedSupplier
 */
public interface ShipModeSelector {

    @NotNull PrioritizedSupplier<ShipMode> getShipModeSupplier();
}
