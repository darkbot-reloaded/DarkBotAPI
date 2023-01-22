package eu.darkbot.api.game.entities;

import org.jetbrains.annotations.ApiStatus;

/**
 * Main static(non-movable) entity in-game
 */
public interface StaticEntity extends Entity {

    /**
     * Pod heal dropped by aegis, hammerclaw, and other ships with special abilities
     */
    interface PodHeal extends StaticEntity {
    }

    /**
     * Capsules picked up by emperor sibelon in the hades galaxy gate
     */
    interface BuffCapsule extends StaticEntity {
    }

    /**
     * Burning trail tech, also used by Plutus in-game
     */
    interface BurningTrail extends StaticEntity {
        /**
         * @return true if that trail was dropped by {@link eu.darkbot.api.managers.HeroAPI}
         * @since 0.7.1
         */
        @ApiStatus.Experimental
        boolean isOwn();
    }

    /**
     * Part of the plutus experiments event. When shot down, spawns a boss npc.
     */
    interface PlutusGenerator extends StaticEntity {

        /**
         * @return true if type of generator is Heal(green)
         * @since 0.7.1
         */
        boolean isHealType();
    }
}
