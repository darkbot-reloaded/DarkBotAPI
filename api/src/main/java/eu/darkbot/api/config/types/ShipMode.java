package eu.darkbot.api.config.types;

import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.managers.HeroAPI;

/**
 * Represent a config mode, that the ship can run in
 * This is the combination of an in-game config and formation.
 *
 * In the future this will ideally support changing configuration &amp; formation
 * but currently it is expected that the results are immutable and non-changing.
 */
public interface ShipMode {

    static ShipMode of(HeroAPI.Configuration configuration, SelectableItem.Formation formation) {
        return new ShipModeImpl(configuration, formation);
    }

    HeroAPI.Configuration getConfiguration();
    SelectableItem.Formation getFormation();

    class ShipModeImpl implements ShipMode {

        private final HeroAPI.Configuration configuration;
        private final SelectableItem.Formation formation;

        public ShipModeImpl(HeroAPI.Configuration configuration, SelectableItem.Formation formation) {
            this.configuration = configuration;
            this.formation = formation;
        }

        @Override
        public HeroAPI.Configuration getConfiguration() {
            return configuration;
        }

        @Override
        public SelectableItem.Formation getFormation() {
            return formation;
        }
    }

}
