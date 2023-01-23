package eu.darkbot.api.game.other;

import eu.darkbot.api.game.entities.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Represents in-game entity which can attack other in-game entities.
 */
public interface Attacker extends Lockable {

    /**
     * @return the target if present, otherwise {@code null}
     */
    @Nullable Entity getTarget();

    default <T extends Entity> @Nullable T getTargetAs(Class<T> type) {
        Entity target = getTarget();
        return type.isInstance(target) ? type.cast(target) : null;
    }

    /**
     * @return true if attacks current target
     * @see #getTarget()
     */
    boolean isAttacking();

    /**
     * @param other {@link Lockable} to check
     * @return true if attacks {@code other}
     */
    default boolean isAttacking(Lockable other) {
        return isAttacking() && Objects.equals(getTarget(), other);
    }
}
