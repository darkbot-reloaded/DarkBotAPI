package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.other.Area;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.util.ArrayUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Provides access to game maps, map sizes, and pathfinding between them.
 */
public interface StarSystemAPI extends API.Singleton {
    List<String> HOME_MAPS         = ArrayUtils.asImmutableList("1-1", "2-1", "3-1");
    List<String> OUTPOST_HOME_MAPS = ArrayUtils.asImmutableList("1-8", "2-8", "3-8");
    List<String> BASE_MAPS         = ArrayUtils.asImmutableList("1-1", "2-1", "3-1", "1-8", "2-8", "3-8");
    List<String> PIRATE_MAPS       = ArrayUtils.asImmutableList("5-1", "5-2", "5-3", "5-4");
    List<String> BLACK_LIGHT_MAPS  = ArrayUtils.asImmutableList("1BL", "2BL", "3BL");

    /**
     * Get the current map the hero is in.
     * This method is equivalent to {@link HeroAPI#getMap()}
     *
     * @return current {@link GameMap} the hero is in
     */
    GameMap getCurrentMap();

    /**
     * @return bounds of the current map
     */
    Area.Rectangle getCurrentMapBounds();

    /**
     * @return {@link Collection} of all known maps
     */
    Collection<? extends GameMap> getMaps();

    /**
     * @return if the map can be jumped to from any other map.
     */
    boolean isAccessible(GameMap map);

    /**
     * Find {@link GameMap} by given {@code mapId} otherwise will create a new one with given mapId.
     *
     * @param mapId id to find
     * @return {@link GameMap} with given {@code mapId}
     * @since 0.7.0
     */
    GameMap getOrCreateMap(int mapId);

    /**
     * Find {@link GameMap} by given {@code mapName} otherwise will create a new one with given mapName.
     *
     * @param mapName map name to find
     * @return {@link GameMap} with given {@code mapId}
     * @since 0.8.3
     */
    GameMap getOrCreateMap(String mapName);

    /**
     * Find optional {@link GameMap} by given {@code mapId}.
     *
     * @param mapId id to find
     * @return Optional {@link GameMap} with given {@code mapId}, {@link Optional#empty()} otherwise
     * @since 0.7.0
     */
    Optional<GameMap> findMap(int mapId);

    /**
     * Find optional {@link GameMap} by given {@code mapName}.
     *
     * @param mapName name to find
     * @return Optional {@link GameMap} with given {@code mapName}, {@link Optional#empty()} otherwise
     * @since 0.7.0
     */
    Optional<GameMap> findMap(String mapName);

    /**
     * Find {@link GameMap} by given {@code mapId}.
     *
     * @param mapId to find
     * @return {@link GameMap} with given {@code mapId}
     * @throws MapNotFoundException if map was not found
     */
    @Deprecated(since = "0.7.0", forRemoval = true)
    default GameMap getById(int mapId) throws MapNotFoundException {
        return findMap(mapId).orElseThrow(() -> new StarSystemAPI.MapNotFoundException(mapId));
    }

    /**
     * Find {@link GameMap} by given {@code mapId} otherwise will create a new one with given mapId.
     *
     * @param mapId to find
     * @return {@link GameMap} with given {@code mapId}
     */
    @Deprecated(since = "0.7.0", forRemoval = true)
    default GameMap getOrCreateMapById(int mapId) {
        return getOrCreateMap(mapId);
    }

    /**
     * Find {@link GameMap} by given {@code mapName}.
     * {@code mapName} must equals searched {@link GameMap#getName()}
     *
     * @param mapName to find
     * @return {@link GameMap} with given {@code mapName}
     * @throws MapNotFoundException if map was not found
     */
    @Deprecated(since = "0.7.0", forRemoval = true)
    default GameMap getByName(@NotNull String mapName) throws MapNotFoundException {
        return findMap(mapName).orElseThrow(() -> new StarSystemAPI.MapNotFoundException(mapName));
    }

    /**
     * Pathfinding through maps towards a goal, this returns the portal
     * to use to get closer to the target.
     * <br>
     * Note: This is a low-level function, use the MapTraveler util instead.
     *
     * @param targetMap target map that you're trying to travel to
     * @return best {@link Portal} that will get you closer to {@code targetMap}
     */
    Portal findNext(@NotNull GameMap targetMap);

    @Deprecated(since = "0.7.0", forRemoval = true)
    class MapNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        public MapNotFoundException(int mapId) {
            super("Map with id " + mapId + " was not found");
        }

        public MapNotFoundException(String mapName) {
            super("Map " + mapName + " was not found");
        }
    }

    @Getter
    class MapChangeEvent implements Event {
        private final GameMap previous;
        private final GameMap next;

        public MapChangeEvent(GameMap previous, GameMap next) {
            this.previous = previous;
            this.next = next;
        }

    }
}
