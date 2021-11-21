package eu.darkbot.api.config.legacy;

import eu.darkbot.api.config.types.PlayerInfo;
import eu.darkbot.api.config.types.SafetyInfo;
import eu.darkbot.api.config.types.ZoneInfo;
import eu.darkbot.api.game.other.GameMap;

import java.util.Collection;
import java.util.Map;

public interface Config {

    /**
     * @param map the map to get infos for
     * @return configured safeties for the requested map
     */
    Collection<? extends SafetyInfo> getSafeties(GameMap map);

    /**
     * Return the preferred zone for a map, creating one if none exists
     *
     * @param map the map to get the zone for
     * @return the zone info for the map
     */
    ZoneInfo getPreferredZone(GameMap map);

    /**
     * Return the avoided zone for a map, creating one if none exists
     *
     * @param map the map to get the zone for
     * @return the zone info for the map
     */
    ZoneInfo getAvoidedZone(GameMap map);

    /**
     * @return map with all the known player infos with the id as key
     */
    Map<Integer, ? extends PlayerInfo> getPlayerInfos();

    /**
     * Return the PlayerInfo for a certain player, creating one if none exists
     * @param playerId id of the player to search
     * @return the player info for the player
     */
    PlayerInfo getPlayerInfo(int playerId);

    /**
     * Send an update to the view for player infos
     * This should be called after any modification to player data to update the view.
     */
    void refreshPlayerList();

    /**
     * @return general configuration section with generic base configurations
     */
    General getGeneral();

    /**
     * @return collect configuration section with configurations about how to collect
     */
    Collect getCollect();

}
