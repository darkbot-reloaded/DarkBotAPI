package eu.darkbot.api.game.items;

public interface ItemTimer {

    Type getTimerType();

    default boolean isActivated() {
        return getTimerType() == Type.ACTIVATED;
    }

    double getTotalCoolingTime();

    double getTimeElapsed();
    double getAvailableIn();

    enum Type {
        ACTIVATED,
        COOLING_DOWN
    }
}
