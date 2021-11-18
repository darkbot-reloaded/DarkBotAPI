package eu.darkbot.api.game.entities;

import java.util.Optional;

/**
 * In-game pet entity flying on the map
 */
public interface Pet extends Ship {

    /**
     * @return level of the {@link Pet}
     */
    int getLevel();

    /**
     * @return userId of the {@link Pet}'s owner
     */
    int getOwnerId();

    /**
     * @return owner of this pet if present, {@link Optional#empty()} otherwise
     */
    Optional<Ship> getOwner();
}
