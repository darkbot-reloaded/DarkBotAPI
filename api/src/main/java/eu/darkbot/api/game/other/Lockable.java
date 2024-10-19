package eu.darkbot.api.game.other;

import eu.darkbot.api.game.entities.Entity;
import eu.darkbot.api.managers.HeroAPI;

/**
 * An entity that can be locked &amp; attacked
 */
public interface Lockable extends Entity {

    /**
     * This method can return true for any non-attacked entities by other players.
     *
     * @return if this {@link Lockable} is owned by {@link HeroAPI}
     */
    default boolean isOwned() {
        return getLockType() == Lock.OWNED;
    }

    /**
     * Check lock type for this {@link Lockable}
     *
     * @return {@link Lock}
     */
    Lock getLockType();

    /**
     * @return The health representation of this attackable
     */
    Health getHealth();

    /**
     * @return The info representation of this attackable
     */
    EntityInfo getEntityInfo();

    /**
     * In-game lock on the selected entity
     */
    enum Lock {

        /**
         * Unknown
         */
        UNKNOWN,

        /**
         * Owned by {@link HeroAPI}.
         * Known as red circle around the target.
         */
        OWNED,

        /**
         * Owned by someone else.
         * Known as gray circle around the target.
         */
        NOT_OWNED,

        /**
         * Citadel's draw fire ability.
         */
        PURPLE,

        /**
         * ?
         */
        GRAY_DARK;

        private static final Lock[] VALUES = values();

        public static Lock of(int lockId) {
            if (lockId >= VALUES.length || lockId < 0) return UNKNOWN;
            return VALUES[lockId];
        }
    }

}
