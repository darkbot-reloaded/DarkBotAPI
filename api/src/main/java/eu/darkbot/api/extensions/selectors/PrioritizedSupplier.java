package eu.darkbot.api.extensions.selectors;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface PrioritizedSupplier<T> extends Supplier<T> {

    /**
     * In case when this method returns null, the supplier will be skipped
     */
    @Nullable
    default Priority getPriority() {
        return Priority.LOWEST;
    }

    enum Priority {
        LOWEST, LOW, MODERATE, HIGH, HIGHEST
    }
}
