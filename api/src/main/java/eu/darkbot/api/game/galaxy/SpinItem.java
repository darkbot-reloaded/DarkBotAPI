package eu.darkbot.api.game.galaxy;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface SpinItem {

    @Nullable Instant getDate();

    @Nullable String getState();
    @Nullable String getType();

    @Nullable Integer getGateId();
    @Nullable Integer getDuplicate();
    @Nullable Integer getPartId();
    @Nullable Integer getItemId();
    @Nullable Integer getAmount();
    @Nullable Integer getCurrent();
    @Nullable Integer getTotal();
    @Nullable Integer getMultiplierUsed();
}
