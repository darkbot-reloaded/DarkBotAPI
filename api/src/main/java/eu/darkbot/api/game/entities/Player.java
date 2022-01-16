package eu.darkbot.api.game.entities;

import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * In-game player ship
 */
public interface Player extends Ship {
    /**
     * @return ship type in-game like `ship_venom_design_frozen`
     */
    String getShipType();

    /**
     * @return if this ship has a {@link Pet} enabled flying on the map.
     */
    boolean hasPet();

    /**
     * @return {@link Pet} associated with this player otherwise {@link Optional#empty()}.
     */
    Optional<Pet> getPet();

    /**
     * @return the {@link SelectableItem.Formation} currently in use by this {@link Player},
     * or {@link SelectableItem.Formation#STANDARD} otherwise.
     */
    SelectableItem.Formation getFormation();

    /**
     * @param formationId formation to check
     * @return if the player is flying the given formation by id.
     */
    boolean isInFormation(int formationId);

    /**
     * @param formation formation to check
     * @return if the player is flying the given formation.
     */
    default boolean isInFormation(@NotNull SelectableItem.Formation formation) {
        return isInFormation(formation.ordinal());
    }
}
