package eu.darkbot.api.game.other;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public interface Location extends Locatable {

    /**
     * Creates new instance of {@link Location} with given parameters.
     * @param x coordinate
     * @param y coordinate
     * @return {@link Location} with given coordinates
     */
    static Location of(double x, double y) {
        return new LocationImpl(x, y);
    }

    static Location of(Locatable locatable) {
        return of(locatable.getX(), locatable.getY());
    }

    /**
     * Will create new instance of {@link Location} around the center point based on angle &amp; radius.
     *
     * @param center point around where new location will be calculated
     * @param angle radians angle
     * @param radius distance from the center location
     * @return new {@link Location} around the center point
     */
    static Location of(@NotNull Locatable center, double angle, double radius) {
        return Location.of(center.getX() - Math.cos(angle) * radius,
                           center.getY() - Math.sin(angle) * radius);
    }

    /**
     * Copies current location into a new {@link Location} object and returns it.
     * @return a new {@link Location} object with same coordinates as current
     */
    default Location copy() {
        return Location.of(this);
    }

    /**
     * Sets current location into specified location.
     * @param x new x coordinate for this location
     * @param y new y coordinate for this location
     * @return this {@link Location} with new coordinates
     */
    Location setTo(double x, double y);

    default Location setTo(@NotNull Locatable other) {
        return setTo(other.getX(), other.getY());
    }

    /**
     * Adds given location to current.
     * Equals {@code currentX + plusX, currentY + plusY}
     */
    default Location plus(double plusX, double plusY) {
        return setTo(getX() + plusX, getY() + plusY);
    }

    default Location plus(@NotNull Locatable other) {
        return plus(other.getX(), other.getY());
    }

    /**
     * Will set to location around center point based on angle &amp; radius.
     *
     * @param center point around where new location will be calculated
     * @param angle radians angle
     * @param radius distance from the center location
     * @return this {@link Location} with new coordinates
     */
    default Location toAngle(@NotNull Locatable center, double angle, double radius) {
        return setTo(center.getX() - Math.cos(angle) * radius,
                center.getY() - Math.sin(angle) * radius);
    }

    default Location toAngle(double angle, double radius) {
        return toAngle(this, angle, radius);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter(AccessLevel.NONE)
    class LocationImpl implements Location {
        public double x;
        public double y;

        @Override
        public Location setTo(double x, double y) {
            this.x = x;
            this.y = y;
            return this;
        }
    }
}
