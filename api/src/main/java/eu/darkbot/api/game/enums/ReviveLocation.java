package eu.darkbot.api.game.enums;

import eu.darkbot.util.ArrayUtils;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public enum ReviveLocation {
    // makes user logout
    BACKPAGE(0, 9),
    BASE(1, 5, 6),
    PORTAL(2),
    SPOT(3, 11),
    SPAWN_POINT(4, 7, 8, 10, 12);

    private static final ReviveLocation[] VALUES = values();

    private final List<Integer> ids;

    ReviveLocation(Integer... ids) {
        this.ids = ArrayUtils.asImmutableList(ids);
    }

    public static ReviveLocation of(int id) {
        for (ReviveLocation loc : VALUES) {
            if (loc.ids.contains(id))
                return loc;
        }

        throw new IllegalStateException("Provided unsupported revive location id: " + id);
    }

    @Unmodifiable
    public List<Integer> getIds() {
        return ids;
    }
}
