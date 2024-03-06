package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public interface FrozenLabyrinthAPI extends API.Singleton {

    /**
     * Get how long until the current {@link #getStatus()} changes.
     * @return how long until the lab opens/closes, in seconds
     */
    double getRemainingTime();

    /**
     * In-game keys, they are required to jump portals inside the labyrinth
     * @return Amount of keys available
     */
    int getKeyCount();

    /**
     * @return the current status of the frozen labyrinth
     */
    Status getStatus();

    /**
     * @return current map the synk is in, if any
     */
    @Nullable MapZone getSynkMapZone();

    /**
     * The current status of the labyrinth
     */
    enum Status {
        OPEN, CLOSED, ENDED
    }

    /**
     * List of all the maps inside the labyrinth.
     * You can convert them to GameMap in {@link StarSystemAPI}
     */
    enum LabMap {
        ATLAS_A  (430),
        ATLAS_B  (431),
        ATLAS_C  (432),
        CYGNI    (433),
        HELVETIOS(434),
        ERIDANI  (435),
        SIRIUS   (436),
        SADATONI (437),
        PERSEI   (438),
        VOLANTIS (439),
        ALCYONE  (440),
        AURIGA   (441),
        BOOTES   (442),
        AQUILA   (443),
        ORION    (444),
        MAIA     (445);

        private final int mapId;
        private final MapZone[] zones = new MapZone[4];

        LabMap(int mapId) {
            this.mapId = mapId;
            for (int i = 0; i < 4; i++) {
                zones[i] = new MapZone(this, Zone.values()[i]);
            }
        }

        /**
         * Get the in-game map id, same as a corresponding {@link eu.darkbot.api.game.other.GameMap}
         * @return the in-game map id
         */
        public int getMapId() {
            return mapId;
        }

        /**
         * Get a MapZone for this LabMap with one of the 4 corners
         * @param corner The zone for the corner to get
         * @return The map zone for this lab map + zone combination.
         */
        public MapZone z(Zone corner) {
            return z(corner.ordinal());
        }

        /**
         * Get a MapZone for this LabMap with one of the 4 corners
         * @param corner The corner, must be one of 0, 1, 2 or 3
         * @return The map zone for this lab map + zone combination.
         */
        public MapZone z(int corner) {
            if (corner < 0 || corner >= zones.length)
                throw new IllegalArgumentException("Corner must be between 0 and 3");
            return zones[corner];
        }
    }

    /**
     * One of the 4 zones in the maps
     */
    @Getter
    @AllArgsConstructor
    enum Zone {
        TOP_LEFT("TL"),
        TOP_RIGHT("TR"),
        BOTTOM_LEFT("BL"),
        BOTTOM_RIGHT("BR");

        private final String tinyName;
    }

    /**
     * The quadrant or zone for a specific map
     */
    @Getter
    class MapZone {
        private final LabMap map;
        private final Zone zone;

        /**
         * Get instances via {@link LabMap#z(Zone)} or {@link LabMap#z(int)}
         * @param map the map represented
         * @param zone the zone inside the map
         */
        private MapZone(LabMap map, Zone zone) {
            this.map = map;
            this.zone = zone;
        }

        @Override
        public String toString() {
            return map.name().replace("_", " ") + " " + zone.ordinal();
        }
    }

}
