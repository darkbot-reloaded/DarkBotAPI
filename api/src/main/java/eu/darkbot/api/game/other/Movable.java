package eu.darkbot.api.game.other;

import eu.darkbot.api.game.entities.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * In-game entity that is able to move on the map
 */
public interface Movable extends Entity {

    /**
     * @return true if {@link Movable} is moving in-game
     */
    @Override
    boolean isMoving();

    /**
     * @param inTime time in milliseconds
     * @return true if {@link Movable} is moving or was moving in given time
     */
    boolean isMoving(long inTime);

    /**
     * @return speed of the {@link Movable} in-game.
     */
    int getSpeed();

    /**
     * Reads in-game angle which is, where entity looks at, not where it flies to.
     * <br>Value is in the range of <i>pi</i> to <i>2pi</i>
     *
     * @return angle of the {@link Movable} in-game as radians.
     */
    double getAngle();

    /**
     * Comparing with {@link Double#MIN_VALUE} means that angle was never updated.
     * <br>Value is in the range of -<i>pi</i> to <i>pi</i>
     *
     * @return angle based on current position and destination, is rotated by 180° compared to {@link LocationInfo#getAngle()}
     */
    double getDestinationAngle();

    /**
     * @param other the other locatable to test against
     * @return true if this entity is visually aiming at another by checking the angle
     * @see #getAngle()
     */
    boolean isAiming(@NotNull Locatable other);

    /**
     * @return The current traveling destination of the entity if any, otherwise {@link Optional#empty()}.
     */
    Optional<Location> getDestination();

    /**
     * Calculates needed time to travel given distance.
     *
     * @param distance the distance to travel
     * @return time in milliseconds needed to travel given distance
     */
    default long timeTo(double distance) {
        return (long) (distance * 1000 / getSpeed());
    }

    /**
     * The time it will take for the entity to move to the desired destination
     * @param destination the position to move to
     * @return time in milliseconds needed to reach the destination
     */
    default long timeTo(@NotNull Locatable destination) {
        return timeTo(getLocationInfo().distanceTo(destination));
    }
}
