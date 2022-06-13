package eu.darkbot.api.game.items;

public interface ItemTimer {
    /**
     * @return true if item is activated, false if is cooling down
     */
    boolean isActivated();

    @Deprecated
    double getTotalCoolingTime();

    /**
     * @return total time(ms) of activation/cooling down
     */
    double getTotalTime();

    /**
     * @return time(ms) elapsed after activation/cooling down
     */
    double getTimeElapsed();

    /**
     * @return the amount of time(ms) item will be activated ({@link #isActivated()} == true,
     * or time it takes to cooldown an item
     */
    double getAvailableIn();
}
