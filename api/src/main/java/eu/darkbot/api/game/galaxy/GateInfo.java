package eu.darkbot.api.game.galaxy;

import java.util.Optional;

public interface GateInfo {
    /**
     * @return true if gate can be placed on map
     */
    default boolean canBePlaced() {
        return !isOnMap() && isCompleted();
    }

    /**
     * Checks if gate is completed/all parts have been obtained,
     * basically compares {@link #getCurrentParts()} with {@link #getTotalParts()}.
     *
     * @return true if gate is completed
     */
    default boolean isCompleted() {
        return getCurrentParts() >= getTotalParts();
    }

    /**
     * @return true if {@link GalaxyGate} is placed &amp; available to jump on map
     */
    boolean isOnMap();

    int getTotalParts();
    int getCurrentParts();

    int getTotalWave();
    int getCurrentWave();

    int getLivesLeft();
    int getLifePrice();

    int getMultiplier();

    /**
     * @return non-empty optional if gate has any bonus reward
     */
    Optional<BonusReward> getBonusReward();

    interface BonusReward {
        boolean isClaimed();
        int getAmount();
        int getCountdown();

        String getLootId();
    }
}
