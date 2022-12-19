package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.enums.PetGear;
import org.jetbrains.annotations.Nullable;

/**
 * Specialized {@link PrioritizedSupplier} with additional methods for pet state.
 *
 * @see eu.darkbot.api.extensions.selectors.PrioritizedSupplier
 */
public interface PetGearSupplier extends PrioritizedSupplier<PetGear> {

    /**
     * If the pet should be enabled.
     * @return true to enable, false to disable, null to respect whatever is set by the module
     * @see eu.darkbot.api.managers.PetAPI#setEnabled(boolean)
     */
    @Nullable
    default Boolean enablePet() {
        return null;
    }
}
