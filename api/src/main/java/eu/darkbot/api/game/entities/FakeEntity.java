package eu.darkbot.api.game.entities;

import eu.darkbot.api.game.other.Location;

/**
 * Fake entities help to keep virtual entities on a map that placed out of visibility range.
 * It should use when need to assist or predict position these entities for active module.
 * Cases of use: keeping mine locations for choose a better path and prevent releasing it,
 *               keeping box locations that should be collected after more prefer tasks.
 */
public interface FakeEntity extends Entity {
    static boolean isFakeEntity(Entity entity) {
        return entity instanceof FakeEntity;
    }

    /**
     * Set timeout lifetime of a fake object in ms
     */
    void setTimeout(long keepAlive);

    /**
     * Set the minimum distance to a hero when an entity will be removed
     */
    void setRemoveDistance(long removeDistance);


    /**
     * Fake box is a target for colector module if more prefer boxes is absent
     */
    interface FakeBox extends Box, FakeEntity {
        /**
         * Is remove fake entity after attempt select it
         */
        void setRemoveWhenAttemptSelect(boolean removeWhenAttemptSelect);
    }

    /**
     * Fake mine is an obstacle that counted by path builder
     */
    interface FakeMine extends Mine, FakeEntity {
    }

    /**
     * Fake ship. It can be NPS, player or pet.
     */
    interface FakeShip extends Ship, FakeEntity {

        /**
         * Is remove fake entity after attempt select it
         */
        void setRemoveWhenAttemptSelect(boolean removeWhenAttemptSelect);

        /**
         * Update location of ship
         */
        void setLocation(Location location);
    }
}
