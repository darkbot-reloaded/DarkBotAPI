package eu.darkbot.api.game.entities;

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
    }

    /**
     * Part of the plutus experiments event. When shot down, spawns a boss npc.
     */
    interface PlutusGenerator extends StaticEntity {
    }
}
