package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.managers.BotAPI;
import eu.darkbot.api.managers.ConfigAPI;
import eu.darkbot.api.managers.I18nAPI;
import eu.darkbot.api.utils.Inject;
import eu.darkbot.shared.utils.MapTraveler;

import java.util.function.Consumer;

public class MapModule extends TemporalModule {

    protected final MapTraveler traveler;
    protected final I18nAPI i18n;

    protected Consumer<Object> mapChangeListener;
    protected ConfigSetting<Object> mapChangeSetting;

    public MapModule(PluginAPI api, boolean backOnConfigChange) {
        this(api.requireAPI(BotAPI.class), api.requireInstance(MapTraveler.class), api.requireAPI(I18nAPI.class));

        if (backOnConfigChange) {
            this.mapChangeSetting = api.requireAPI(ConfigAPI.class).requireConfig("general.working_map");
            this.mapChangeSetting.addListener(mapChangeListener = (a -> goBack()));
        }
    }

    @Inject
    public MapModule(BotAPI bot,
                     MapTraveler mapTraveler,
                     I18nAPI i18n) {
        super(bot);
        this.traveler = mapTraveler;
        this.i18n = i18n;
    }

    public void setTarget(GameMap target) {
        this.traveler.setTarget(target);
    }

    @Override
    public void goBack() {
        super.goBack();
        if (mapChangeSetting != null && mapChangeListener != null)
            mapChangeSetting.removeListener(mapChangeListener);
    }

    @Override
    public String getStatus() {
        return traveler.current != null ?
                i18n.get("module.map_travel.status.has_next", traveler.target.getName(),
                        traveler.current.getTargetMap().map(GameMap::getName).orElse("unknown?")) :
                i18n.get("module.map_travel.status.no_next", traveler.target.getName());
    }

    @Override
    public void onTickModule() {
        if (!traveler.isDone()) traveler.tick();
        if (traveler.isDone()) goBack();
    }

}
