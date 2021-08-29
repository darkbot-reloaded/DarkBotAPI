package eu.darkbot.api.game.other;

import eu.darkbot.api.game.entities.Entity;

/**
 * An entity that can be locked &amp; attacked
 */
public interface Attackable extends Entity {

    /**
     * Check lock type for this {@link Attackable}
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
         * Owned by {@link eu.darkbot.api.managers.HeroAPI}.
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

        public static Lock of(int lockId) {
            if (lockId >= values().length || lockId < 0) return UNKNOWN;
            return values()[lockId];
        }
    }

}
