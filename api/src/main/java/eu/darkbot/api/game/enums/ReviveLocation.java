package eu.darkbot.api.game.enums;

public enum ReviveLocation {
    // makes user logout
    BACKPAGE(0, 9),
    BASE(1, 5, 6),
    PORTAL(2),
    SPOT(3, 11),
    SPAWN_POINT(4, 7, 8, 10, 12);

    private final int[] ids;

    ReviveLocation(int... ids) {
        this.ids = ids;
    }

    public static ReviveLocation of(int id) {
        for (ReviveLocation loc : values()) {
            for (int i : loc.ids) {
                if (i == id) {
                    return loc;
                }
            }
        }

        return BASE; // return base if not found
    }

    public int[] getIds() {
        return ids;
    }
}