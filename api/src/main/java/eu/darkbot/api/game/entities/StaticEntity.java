package eu.darkbot.api.game.entities;

/**
 * Main static(non-movable) entity in-game
 */
public interface StaticEntity extends Entity {

    interface PodHeal extends StaticEntity {
    }

    /**
     * Probably dropped by emperor sibeleon in-game
     */
    interface BuffCapsule extends StaticEntity {
    }

    /**
     * Burning trail tech, also used by Plutus in-game
     */
    interface BurningTrail extends StaticEntity {
    }

    interface PlutusGenerator extends StaticEntity {
    }
}
