package eu.darkbot.api.extensions.selectors;

public interface PrioritizedSupplier<T> {

    T getBest();

    default Priority getPriority() {
        return Priority.LOWEST;
    }

    enum Priority {
        LOWEST, LOW, MODERATE, HIGH, HIGHEST
    }
}
