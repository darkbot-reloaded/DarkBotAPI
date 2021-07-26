package eu.darkbot.api.config;

import eu.darkbot.api.game.other.GameMap;

import java.util.Collection;

public interface Config {

    /**
     * @return general configuration section with generic base configurations
     */
    General getGeneral();

    /**
     * @return configured safeties for the requested map
     */
    Collection<? extends SafetyInfo> getSafeties(GameMap map);

}
