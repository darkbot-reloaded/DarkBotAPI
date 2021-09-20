package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.config.types.ShipMode;
import org.jetbrains.annotations.NotNull;

public interface ShipModeSelector {

    @NotNull
    PrioritizedSupplier<ShipMode> getShipModeSupplier();
}
