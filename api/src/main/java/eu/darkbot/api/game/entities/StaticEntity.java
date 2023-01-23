package eu.darkbot.api.game.entities;

import eu.darkbot.api.game.enums.PetGear;
import lombok.Getter;
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
         * @since 0.7.0
         */
        @ApiStatus.Experimental
        boolean isOwn();
    }

    /**
     * Part of the plutus experiments event.
     */
    interface PlutusGenerator extends StaticEntity {

        /**
         * @return true if type of generator is Heal(green)
         * @since 0.7.0
         */
        boolean isHealType();
    }

    /**
     * Pet beacon entity, dropped by {@link Pet}
     *
     * @since 0.7.0
     */
    interface PetBeacon extends StaticEntity {

        Type getType();

        @Getter
        enum Type {
            HP(PetGear.BEACON_HP),
            COMBAT(PetGear.BEACON_COMBAT);

            private final PetGear petGear;

            Type(PetGear petGear) {
                this.petGear = petGear;
            }
        }
    }
}
