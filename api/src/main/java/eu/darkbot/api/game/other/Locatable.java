package eu.darkbot.api.game.other;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents location point in-game.
 */
public interface Locatable {
    /**
     * Creates new instance of {@link Locatable} with given parameters.
     * @param x coordinate
     * @param y coordinate
     * @return {@link Locatable} with given coordinates
     */
    static Locatable of(double x, double y) {
        return new LocatableImpl(x, y);
    }

    /**
     * Will create new instance of {@link Locatable} around the center point based on angle &amp; radius.
     *
     * @param center point around where new location will be calculated
     * @param angle radians angle
     * @param radius distance from the center location
     * @return new {@link Locatable} around the center point
     */
    static Locatable of(@NotNull Locatable center, double angle, double radius) {
        return Locatable.of(center.getX() - Math.cos(angle) * radius,
                center.getY() - Math.sin(angle) * radius);
    }

    /**
     * @return y coordinate of the {@link Locatable}
     */
    double getX();

    /**
     * @return y coordinate of the {@link Locatable}
     */
    double getY();

    default int x() {
        return (int) getX();
    }

    default int y() {
        return (int) getY();
    }

    /**
     * Checks if the x and y coordinates of this {@link Locatable} are the same as another {@link Locatable}.
     *
     * @param other the other {@link Locatable} to compare with
     * @return true if the x and y coordinates are the same, false otherwise
     */
    default boolean isSameAs(Locatable other) {
        return Double.compare(getX(), other.getX()) == 0
                && Double.compare(getY(), other.getY()) == 0;
    }

    /**
     * @param ox x coordinate of the other location
     * @param oy y coordinate of the other location
     * @return the distance between current {@link Location} and other.
     */
    default double distanceTo(double ox, double oy) {
        ox -= getX();
        oy -= getY();
        return Math.sqrt(ox * ox + oy * oy);
    }

    default double distanceTo(@NotNull Locatable other) {
        return distanceTo(other.getX(), other.getY());
    }

    /**
     * @param ox x coordinate of the other location
     * @param oy y coordinate of the other location
     * @return angle to other location as radians.
     */
    default double angleTo(double ox, double oy) {
        return Math.atan2(getY() - oy, getX() - ox);
    }

    default double angleTo(@NotNull Locatable other) {
        return angleTo(other.getX(), other.getY());
    }

    //Locatable implementation
    @Value
    class LocatableImpl implements Locatable {
        double x;
        double y;
    }
}
