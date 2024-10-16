package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.config.types.NpcFlag;
import eu.darkbot.api.config.types.NpcInfo;
import eu.darkbot.api.game.enums.PetGear;
import eu.darkbot.api.managers.PetAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Comparator;

/**
 * Specialized {@link PrioritizedSupplier} with additional methods for pet state.
 *
 * @see eu.darkbot.api.extensions.selectors.PrioritizedSupplier
 */
public interface PetGearSupplier extends PrioritizedSupplier<PetGear> {

    /**
     * If the pet should be enabled.
     *
     * @return true to enable, false to disable, null to respect whatever is set by the module
     * @see eu.darkbot.api.managers.PetAPI#setEnabled(boolean)
     */
    @Nullable default Boolean enablePet() {
        return null;
    }

    /**
     * Retrieves the most suitable {@link PetAPI.LocatorPick} from a given collection of potential NPCs.
     *
     * @param picks A collection of {@link PetAPI.LocatorPick} objects representing potential NPCs.
     * @return The most suitable {@link PetAPI.LocatorPick} from the provided collection,
     *         or {@code null} if no suitable pick is found.
     */
    @ApiStatus.AvailableSince("0.9.6")
    @Nullable default PetAPI.LocatorPick getNpcLocatorPick(@UnmodifiableView Collection<? extends PetAPI.LocatorPick> picks) {
        PetAPI.LocatorPick pick = picks.stream()
                .min(Comparator.comparing(this::getNpcPickPriority, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null);

        return pick == null || getNpcPickPriority(pick) == null ? null : pick;
    }

    /**
     * @return priority of the NPC pick if it should be picked otherwise null
     */
    @ApiStatus.AvailableSince("0.9.6")
    @Nullable default Integer getNpcPickPriority(PetAPI.LocatorPick pick) {
        NpcInfo npc = pick.getNpcInfo();
        return npc != null && npc.getShouldKill() && npc.hasExtraFlag(NpcFlag.PET_LOCATOR) ? npc.getPriority() : null;
    }
}
