package eu.darkbot.api.game.galaxy;

import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.managers.StarSystemAPI;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Every GalaxyGate available in spinner.
 */
public enum GalaxyGate {
    ALPHA  ("alpha",    1,  "α"),
    BETA   ("beta",     2,  "β"),
    GAMMA  ("gamma",    3,  "γ"),
    DELTA  ("delta",    4,  "δ"),
    EPSILON("epsilon",  5,  "ε"),
    ZETA   ("zeta",     6,  "ζ"),
    KAPPA  ("kappa",    7,  "κ"),
    LAMBDA ("lambda",   8,  "λ"),
    KRONOS ("kronos",   12, "Kronos"),
    HADES  ("hades",    13, "Hades"),
    KUIPER ("streuner", 19, "ς");

    private final String name;
    private final int id;
    private final String mapSymbol;

    GalaxyGate(String name, int id, String mapSymbol) {
        this.name = name;
        this.id = id;
        this.mapSymbol = mapSymbol;
    }

    public static GalaxyGate of(String gateName) {
        for (GalaxyGate gate : values())
            if (gate.name.equals(gateName))
                return gate;

            return null;
    }

    public static GalaxyGate of(int gateId) {
        for (GalaxyGate gate : values())
            if (gate.id == gateId)
                return gate;

        return null;
    }

    public String getParam() {
        return "&gateID=" + getId() + "&" + getName() + "=1";
    }

    public String getIdParam() {
        return "&gateID=" + getId();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getMapSymbol() {
        return mapSymbol;
    }

    public boolean isInGate(GameMap map) {
        return map != null && map.getName().contains(mapSymbol);
    }

    public Set<GameMap> getMaps(StarSystemAPI starSystem) {
        return starSystem.getMaps().stream()
                .filter(GameMap::isGG)
                .filter(this::isInGate)
                .collect(Collectors.toSet());
    }
}
