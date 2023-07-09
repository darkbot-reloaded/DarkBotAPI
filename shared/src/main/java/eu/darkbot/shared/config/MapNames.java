package eu.darkbot.shared.config;

import eu.darkbot.api.config.annotations.Dropdown;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.managers.StarSystemAPI;
import eu.darkbot.api.utils.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A dropdown options implementation that gives maps as options, saving the map id.
 */
public class MapNames implements Dropdown.Options<Integer> {
    private final List<Integer> maps;
    private final StarSystemAPI starSystemAPI;

    @Inject
    public MapNames(StarSystemAPI starSystemAPI) {
        this(starSystemAPI, starSystemAPI.getMaps().stream()
                .map(GameMap::getId)
                .filter(id -> id > 0) // Filter out fake maps
                .collect(Collectors.toList()));
    }

    /**
     * Alternative constructor, left to allow for extension by providing your own list of maps or way of filtering.
     * @param starSystemAPI The star system api
     * @param maps The list of map ids to make available.
     */
    public MapNames(StarSystemAPI starSystemAPI, List<Integer> maps) {
        this.starSystemAPI = starSystemAPI;
        this.maps = maps;
    }

    @Override
    public Collection<Integer> options() {
        return maps;
    }

    @Override
    public @NotNull String getText(Integer option) {
        return option == null ? "null" : starSystemAPI.getOrCreateMap(option).getName();
    }

}
