package eu.darkbot.shared.utils;

import eu.darkbot.api.events.EventHandler;
import eu.darkbot.api.events.Listener;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.managers.EntitiesAPI;
import eu.darkbot.api.managers.EventBrokerAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.MovementAPI;
import eu.darkbot.api.managers.PetAPI;
import eu.darkbot.api.managers.StarSystemAPI;

import java.util.Collection;
import java.util.Objects;

public class MapTraveler implements Listener {
    // Time to wait for portals to show-up in the map
    protected static final int PORTAL_LOAD_WAIT = 3000;

    // If the ship moves this much, consider it a teleport (a portal jump)
    protected static final int DISTANCE_TP_MIN = 5000;

    // Extra milliseconds added after jumping/teleporting
    protected static final int EXTRA_WAIT_TIME = 2000;

    // If traveling this or longer, disable pet to save fuel
    protected static final int DISABLE_PET_DISTANCE = 1500;

    protected final EventBrokerAPI eventBroker;

    protected final PetAPI pet;
    protected final HeroAPI hero;
    protected final StarSystemAPI star;
    protected final MovementAPI movement;

    protected final Collection<? extends Portal> portals;

    protected final PortalJumper jumper;

    public Portal current;
    public GameMap target;

    protected int lastPortals;
    protected long shipTpWait = -1;
    protected long mapChangeWait = -1;
    protected boolean done;

    public MapTraveler(PetAPI petApi,
                       HeroAPI heroApi,
                       StarSystemAPI starSystem,
                       MovementAPI movement,
                       PortalJumper jumper,
                       EntitiesAPI entities,
                       EventBrokerAPI eventBroker) {
        this.pet = petApi;
        this.hero = heroApi;
        this.star = starSystem;
        this.movement = movement;
        this.jumper = jumper;
        this.portals = entities.getPortals();
        this.eventBroker = eventBroker;
    }

    public void setTarget(GameMap target) {
        shipTpWait = mapChangeWait = -1;
        this.target = target;
        this.current = null;
        this.done = false;
        this.jumper.reset();
    }

    public boolean isDone() {
        return done;
    }

    public void tick() {
        if (Objects.equals(star.getCurrentMap(), target)) {
            done = true;
            return;
        }

        if (shouldWaitAfterTp()) return;

        if (current == null || !current.isValid() || lastPortals != portals.size()) {
            current = star.findNext(target);
            lastPortals = portals.size();
            jumper.reset();
        }

        if (current == null) {
            if (System.currentTimeMillis() - mapChangeWait > PORTAL_LOAD_WAIT)
                done = true; // No port found after 3 secs, just go back.
            return;
        }
        shipTpWait = mapChangeWait = -1;

        // Portal very close, no need to disable pet
        if (current.getLocationInfo().distanceTo(hero) > DISABLE_PET_DISTANCE)
            pet.setEnabled(false);
        hero.setRunMode();

        jumper.travelAndJump(current);
    }

    protected boolean shouldWaitAfterTp() {
        if (hero.getLocationInfo().getLast().distanceTo(hero) > DISTANCE_TP_MIN)
            shipTpWait = System.currentTimeMillis() + EXTRA_WAIT_TIME;

        if ((shipTpWait == -1) != (mapChangeWait == -1)) {
            return System.currentTimeMillis() < Math.max(shipTpWait, mapChangeWait);
        }
        return false;
    }

    @EventHandler
    public void onMapChange(StarSystemAPI.MapChangeEvent event) {
        mapChangeWait = System.currentTimeMillis() + EXTRA_WAIT_TIME;
        lastPortals = -1;
    }

}
