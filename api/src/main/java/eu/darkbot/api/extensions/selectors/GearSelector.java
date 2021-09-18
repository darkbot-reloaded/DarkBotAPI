package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.enums.PetGear;
import org.jetbrains.annotations.Nullable;

//maybe name it PetSupplier and have here also `getPetEnabled()`
public interface GearSelector {

    @Nullable
    PrioritizedSupplier<PetGear> getGearSupplier();
}
