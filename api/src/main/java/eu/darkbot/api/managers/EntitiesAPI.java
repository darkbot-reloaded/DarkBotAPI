package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.game.entities.Barrier;
import eu.darkbot.api.game.entities.BattleStation;
import eu.darkbot.api.game.entities.Box;
import eu.darkbot.api.game.entities.Entity;
import eu.darkbot.api.game.entities.FakeEntity;
import eu.darkbot.api.game.entities.Mine;
import eu.darkbot.api.game.entities.Mist;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.entities.Ore;
import eu.darkbot.api.game.entities.Pet;
import eu.darkbot.api.game.entities.Player;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.entities.Relay;
import eu.darkbot.api.game.entities.Ship;
import eu.darkbot.api.game.entities.SpaceBall;
import eu.darkbot.api.game.entities.StaticEntity;
import eu.darkbot.api.game.entities.Station;
import eu.darkbot.api.game.other.Obstacle;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

/**
 * Provides access to the different types of entities in the map.
 * <p>
 * This is arguably the most useful API in the whole bot, as it provides
 * the means to get information about all entities (ships, npcs, etc.) the map.
 * <p>
 * The instances provided in all methods are automatically updated by the
 * bot during runtime, and are unmodifiable collections.
 */
public interface EntitiesAPI extends API.Singleton {

    /**
     * @return All npcs visible in the map
     * @see Npc
     */
    @UnmodifiableView
    Collection<? extends Npc> getNpcs();

    /**
     * @return All other player pets visible on the map
     * @see Pet
     */
    @UnmodifiableView
    Collection<? extends Pet> getPets();

    /**
     * @return All other players visible on the map
     * @see Ship
     */
    @UnmodifiableView
    Collection<? extends Player> getPlayers();

    /**
     * @return All other players or pets visible on the map.
     * You can use {@link #getPlayers()} and {@link #getPets()} to get them separately
     * @see Ship
     * @see Pet
     */
    @UnmodifiableView
    Collection<? extends Ship> getShips();

    /**
     * @return All boxes or ore resources visible on the map
     * @see Box
     * @see Ore
     */
    @UnmodifiableView
    Collection<? extends Box> getBoxes();

    /**
     * @return All mines visible on the map
     * @see Mine
     */
    @UnmodifiableView
    Collection<? extends Mine> getMines();

    /**
     * @return All portals available on the map.
     * @see Portal
     */
    @UnmodifiableView
    Collection<? extends Portal> getPortals();

    /**
     * @return All home base stations, quest giver, and other demilitarized zones
     * @see Station for all the different types of bases
     */
    @UnmodifiableView
    Collection<? extends Station> getStations();

    /**
     * @return All clan base stations parts in the map, including the modules
     * @see BattleStation
     * @see BattleStation.Module
     */
    @UnmodifiableView
    Collection<? extends BattleStation> getBattleStations();

    /**
     * @return All static relays, seen mainly in LoW map.
     * @see Relay
     */
    @UnmodifiableView
    Collection<? extends Relay> getRelays();

    /**
     * @return All {@link SpaceBall}s visible on the map.
     * @see SpaceBall
     */
    @UnmodifiableView
    Collection<? extends SpaceBall> getSpaceBalls();

    /**
     * @return All {@link StaticEntity}s visible on the map
     * @see StaticEntity
     */
    @UnmodifiableView
    Collection<? extends StaticEntity> getStaticEntities();

    /**
     * @return All entities that are detected by the bot, but not yet categorized into any other collection.
     * Keep in mind whatever is in unknown now may always be properly categorized at a later date.
     */
    @UnmodifiableView
    Collection<? extends Entity> getUnknown();

    /**
     * A collection view of a few other entities, that implement the {@link Obstacle} interface,
     * like clan base stations or mines.
     * <p>
     * Keep in mind other (non-entity) obstacles may still exist outside of this, eg: player defined avoid areas.
     *
     * @return All entities implementing the {@link Obstacle} interface.
     * @see Obstacle
     */
    @UnmodifiableView
    Collection<? extends Obstacle> getObstacles();

    /**
     * @return All {@link Mist}s
     */
    @UnmodifiableView
    Collection<? extends Mist> getMists();

    /**
     * @return All {@link Barrier}s
     */
    @UnmodifiableView
    Collection<? extends Barrier> getBarriers();

    /**
     * Keep in mind this is a special case in this API, as this list is NOT
     * guaranteed to be updated on runtime like all others.
     * <p>
     * For performance reasons it is not advised to use this method unless necessary.
     *
     * @return A new collection with ALL the entities provided by all the methods in this API, including unknown.
     */
    @UnmodifiableView
    Collection<? extends Entity> getAll();

    /**
     * @return a builder to create fake entities
     */
    @ApiStatus.AvailableSince("0.9.6")
    FakeEntity.Builder fakeEntityBuilder();

    /**
     * Base entity event triggered whenever any entity is added or removed.
     * To listen only for creation or removal, use the more specific events:
     *
     * @see EntityCreateEvent
     * @see EntityRemoveEvent
     */
    abstract class EntityEvent implements Event {
        private final Entity entity;

        public EntityEvent(Entity entity) {
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }
    }

    /**
     * Event fired when an entity is created (when it appears on the map)
     */
    class EntityCreateEvent extends EntityEvent {
        public EntityCreateEvent(Entity entity) {
            super(entity);
        }
    }

    /**
     * Event fired when an entity is removed (when it disappears from the map)
     */
    class EntityRemoveEvent extends EntityEvent {
        public EntityRemoveEvent(Entity entity) {
            super(entity);
        }
    }

}
