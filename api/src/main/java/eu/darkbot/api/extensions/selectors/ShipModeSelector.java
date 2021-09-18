package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.config.types.ShipMode;
import org.jetbrains.annotations.Nullable;

public interface ShipModeSelector {

    @Nullable
    PrioritizedSupplier<ShipMode> getShipModeSupplier();
}
