package eu.darkbot.api.extensions.selectors;

import org.jetbrains.annotations.NotNull;

/**
 * Provider of a supplier to use when deciding pet gear modules
 * @see PetGearSupplier
 */
public interface GearSelector {

    @NotNull PetGearSupplier getGearSupplier();
}
