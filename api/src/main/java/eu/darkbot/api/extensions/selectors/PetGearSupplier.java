package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.enums.PetGear;
import org.jetbrains.annotations.Nullable;

public interface PetGearSupplier extends PrioritizedSupplier<PetGear> {

    @Nullable
    default Boolean enablePet() {
        return null;
    }
}
