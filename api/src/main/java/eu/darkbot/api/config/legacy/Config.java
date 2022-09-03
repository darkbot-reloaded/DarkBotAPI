package eu.darkbot.api.config.legacy;

import eu.darkbot.api.config.types.BoxInfo;
import eu.darkbot.api.config.types.NpcInfo;
import eu.darkbot.api.config.types.PlayerInfo;
import eu.darkbot.api.config.types.SafetyInfo;
import eu.darkbot.api.config.types.ZoneInfo;
import eu.darkbot.api.game.other.GameMap;

import java.util.Collection;
import java.util.Map;

/**
 * Methods to interact with legacy configurations that are not easy to manage from new api.
 */
public interface Config {

    /**
     * Will get or create BoxInfo with given box name
     *
     * @param name of the box
     * @return BoxInfo of given box name
     */
    BoxInfo getOrCreateBoxInfo(String name);

    /**
     * Will get or create NpcInfo with given npc name
     *
     * @param name of the npc
     * @return NpcInfo of given npc name
     */
    NpcInfo getOrCreateNpcInfo(String name);

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

}
