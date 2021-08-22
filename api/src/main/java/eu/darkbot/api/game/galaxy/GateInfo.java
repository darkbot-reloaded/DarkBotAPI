package eu.darkbot.api.game.galaxy;

import org.jetbrains.annotations.Nullable;

public interface GateInfo {

    default boolean isFinished() {
        return getCurrent() != null && getCurrent().equals(getTotal());
    }

    default boolean shouldStopSpinning() {
        return getLivesLeft() != null && getLivesLeft() > 0 && isFinished();
    }

    @Nullable String getState();

    @Nullable Integer getTotal();
    @Nullable Integer getCurrent();
    @Nullable Integer getId();
    @Nullable Integer getPrepared();
    @Nullable Integer getTotalWave();
    @Nullable Integer getCurrentWave();
    @Nullable Integer getLivesLeft();
    @Nullable Integer getLifePrice();
    Integer getMultiplier();
}
