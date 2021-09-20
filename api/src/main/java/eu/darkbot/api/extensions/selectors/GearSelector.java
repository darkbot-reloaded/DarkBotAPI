package eu.darkbot.api.extensions.selectors;

import org.jetbrains.annotations.NotNull;

public interface GearSelector {

    @NotNull
    PetGearSupplier getGearSupplier();
}
