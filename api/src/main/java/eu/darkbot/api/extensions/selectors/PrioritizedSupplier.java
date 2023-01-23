package eu.darkbot.api.extensions.selectors;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Prioritized supplier is a generic type of class, which will be called when taking a decision.
 * The suppliers will be called to decide what to use (eg: what ammo to shoot with, or what pet module to use),
 * and the highest priority supplier will be used.
 *
 * @param <T> The type of the supplier
 */
public interface PrioritizedSupplier<T> extends Supplier<T> {

    /**
     * The priority this supplier should be used with. A null priority means it should not be used.
     * @return the priority of the supplier at the current time
     */
    @Nullable default Priority getPriority() {
        return Priority.LOWEST;
    }

    /**
     * Gets the current result of the supplier
     * @return the result to use
     */
    @Override
    T get();

    enum Priority {
        LOWEST, LOW, MODERATE, HIGH, HIGHEST
    }
}
