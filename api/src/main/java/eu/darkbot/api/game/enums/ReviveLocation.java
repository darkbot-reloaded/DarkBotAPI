package eu.darkbot.api.game.enums;

public enum ReviveLocation {
    BASE,
    PORTAL,
    SPOT;

    public long getId() {
        return ordinal() + 1;
    }
}