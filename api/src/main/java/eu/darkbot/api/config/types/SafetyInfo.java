package eu.darkbot.api.config.types;

import eu.darkbot.api.game.entities.Entity;
import eu.darkbot.api.game.other.Locatable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface SafetyInfo extends Locatable {

    enum Type {
        PORTAL, CBS, BASE;
    }

    /**
     * Running reasons this safety can be selected
     */
    enum RunMode {
        ALWAYS, ENEMY_FLEE_ONLY, REPAIR_ONLY, REFRESH, NEVER
    }

    /**
     * Conditions to jump on portals
     */
    enum JumpMode {
        NEVER, ESCAPING, FLEEING, REPAIRING, ALWAYS, ALWAYS_OTHER_SIDE
    }

    /**
     * Conditions to use if they are CBS (clan base stations)
     */
    enum CbsMode {
        ALLY, ALLY_NEUTRAL;
    }

    /**
     * @return The type of safety this is, a portal, cbs or a base
     */
    Type getType();

    /**
     * @return The run mode the user configured
     */
    RunMode getRunMode();

    /**
     * @param runMode the new run mode to use
     */
    void setRunMode(RunMode runMode);

    /**
     * @return the defined {@link JumpMode} for portals, null otherwise
     */
    @Nullable JumpMode getJumpMode();

    /**
     * @return the defined {@link CbsMode} for CBS, null otherwise
     */
    @Nullable CbsMode getCbsMode();

    /**
     * @return X location of this safety
     */
    @Override double getX();

    /**
     * @return Y location of this safety
     */
    @Override double getY();

    /**
     * @return diameter of the safe zone, they are always assumed to be circular
     */
    int getDiameter();

    /**
     * @return the radius of the safe zone, half of the {@link #getDiameter()}
     */
    default int getRadius() {
        return getDiameter() / 2;
    }

    /**
     * @return entity representing this safety, like the portal or CBS
     */
    Optional<Entity> getEntity();

    /**
     * Set a distance value to this safety, later read from {@link #getDistance}
     *
     * This is used as a cache to store calculated distances and avoid re-computing them
     *
     * @param distance how far away the hero ship is from this safety
     */
    void setDistance(double distance);

    /**
     * @return the last set distance value, see {@link #setDistance(double)}
     */
    double getDistance();

}
