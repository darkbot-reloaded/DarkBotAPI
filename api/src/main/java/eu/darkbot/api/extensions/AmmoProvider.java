package eu.darkbot.api.extensions;

import eu.darkbot.api.game.items.SelectableItem;

public interface AmmoProvider {

    SelectableItem.Laser getLaser();

    default Priority getPriority() {
        return Priority.LOWEST;
    }

    enum Priority {
        LOWEST, LOW, MODERATE, HIGH, HIGHEST
    }
}
