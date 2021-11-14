package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.NotNull;

public interface LaserSelector {

    @NotNull
    PrioritizedSupplier<SelectableItem.Laser> getLaserSupplier();
}
