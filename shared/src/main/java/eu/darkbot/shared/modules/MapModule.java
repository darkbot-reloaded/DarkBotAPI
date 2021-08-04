package eu.darkbot.shared.modules;

import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.managers.BotAPI;
import eu.darkbot.api.managers.I18nAPI;
import eu.darkbot.shared.utils.MapTraveler;

public class MapModule extends TemporalModule {

    protected final MapTraveler traveler;
    protected final I18nAPI i18n;

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
