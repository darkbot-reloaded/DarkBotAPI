package eu.darkbot.api.game.entities;

import eu.darkbot.api.game.other.Attacker;
import eu.darkbot.api.game.other.Lockable;
import eu.darkbot.api.game.other.Obstacle;

import java.util.Locale;

/**
 * Default battle station entity
 */
public interface BattleStation extends Obstacle, /*deprecated*/ Lockable {

    /**
     * @deprecated use {@link Hull#getHullId()} instead
     */
    @Deprecated int getHullId();

    /**
     * Asteroid station - empty, non built
     */
    interface Asteroid extends BattleStation {
    }

    /**
     * Main battle station entity, known as CBS
     */
    interface Hull extends BattleStation, Lockable {

        @Override
        int getHullId();
        double getHullExpansion();

        int getDeflectorShieldId();
        double getDeflectorShieldExpansion();
    }

    /**
     * In-game battle station modules around the main battle station
     */
    interface Module extends BattleStation, Attacker {

        /**
         * @return the in-game id of the module
         */
        String getModuleId();

        Type getType();

        enum Type {
            WRECK(),
            HULL(),
            DEFLECTOR(),
            REPAIR("rep"),
            LASER_HR("laserhigh"),
            LASER_MR("lasermid"),
            LASER_LR("laserlow"),
            ROCKET_LA("rocketlow"),
            ROCKET_MA("rocketmid"),
            HONOR_BOOSTER("hon"),
            DAMAGE_BOOSTER("dama"),
            EXPERIENCE_BOOSTER("xp");

            private static final Type[] VALUES = values();

            private final String id;

            Type(String id) {
                this.id = id == null ? name().toLowerCase(Locale.ROOT) : id;
            }

            Type() {
                this(null);
            }

            public static Type of(String moduleId) {
                for (Type value : VALUES) {
                    if (moduleId.contains(value.id))
                        return value;
                }
                return null;
            }
        }
    }
}
