package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.Nullable;

public interface LaserSelector {

    @Nullable
    PrioritizedSupplier<SelectableItem.Laser> getLaserSupplier();
}
