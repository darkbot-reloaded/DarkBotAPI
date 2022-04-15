package eu.darkbot.api.extensions.selectors;

import eu.darkbot.api.game.enums.ReviveLocation;
import org.jetbrains.annotations.NotNull;

public interface ReviveSelector {

    @NotNull
    PrioritizedSupplier<ReviveLocation> getReviveLocationSupplier();
}
