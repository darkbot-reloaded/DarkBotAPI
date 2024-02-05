package eu.darkbot.api.game.other;


/**
 * Represents in-game {@link eu.darkbot.api.game.entities.Entity}'s location point.
 */
public interface LocationInfo extends Location {

    /**
     * @return true if current entity is moving.
     */
    boolean isMoving();

    /**
     * Speed returned by this method is divided by 1000 compared to {@link Movable#getSpeed()}<br>
     * For example, returned value {@code 0.5} is equivalent to 500 in-game
     *
     * @return speed of entity based on traveled distance or 0 if entity isn't moving.
     */
    double getSpeed();

    /**
     * Value is in the range of -<i>pi</i> to <i>pi</i>
     *
     * @return current entity angle as radians.
     */
    double getAngle();

    /**
     * Calculates future destination of entity in time(ms), if entity isn't moving returns copy of location.
     */
    default Location destinationInTime(long time) {
        Location result = copy();

        double speed = getSpeed();
        if (speed > 0) {
            double move = speed * time;
            double angle = getAngle();
            result.plus(Math.cos(angle) * move, Math.sin(angle) * move);
        }
        return result;
    }

    /**
     * @return current {@link Location} of this {@link LocationInfo}
     */
    Location getCurrent();

    /**
     * @return previous {@link Location} of this {@link LocationInfo}
     */
    Location getLast();

    /**
     * @return even earlier {@link Location} of this {@link LocationInfo}
     */
    Location getPast();

    /**
     * If this location info has been initialized to a memory address.
     * If false the x &amp; y coordinates will likely point to 0,0
     * @return true if this location info has been initialized, false otherwise
     */
    boolean isInitialized();

    @Override
    default double getX() {
        return getCurrent().getX();
    }

    @Override
    default double getY() {
        return getCurrent().getY();
    }

    @Override
    default Location copy() {
        return getCurrent().copy();
    }

    @Override
    default Location setTo(double x, double y) {
        return getCurrent().setTo(x, y);
    }
}
