package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.game.entities.Box;
import eu.darkbot.api.game.entities.Player;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.enums.EntityEffect;
import eu.darkbot.api.game.items.ItemFlag;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Location;
import eu.darkbot.api.managers.BotAPI;
import eu.darkbot.api.managers.ConfigAPI;
import eu.darkbot.api.managers.EntitiesAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import eu.darkbot.api.managers.MovementAPI;
import eu.darkbot.api.managers.PetAPI;
import eu.darkbot.api.managers.StarSystemAPI;
import eu.darkbot.api.managers.StatsAPI;
import eu.darkbot.api.utils.Inject;
import eu.darkbot.shared.utils.SafetyFinder;

import java.util.Collection;
import java.util.Comparator;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

@Feature(name = "Collector", description = "Resource-only collector module. Can cloak.")
public class CollectorModule implements Module {

    protected static final int DISTANCE_FROM_DANGEROUS = 1500;

    protected final PluginAPI api;
    protected final BotAPI bot;
    protected final PetAPI pet;
    protected final HeroAPI hero;
    protected final StatsAPI stats;
    protected final MovementAPI movement;
    protected final HeroItemsAPI items;
    protected final StarSystemAPI starSystem;

    protected final SafetyFinder safetyFinder;

    protected final Collection<? extends Box> boxes;
    protected final Collection<? extends Player> players;
    protected final Collection<? extends Portal> portals;

    protected final ConfigSetting<Integer> workingMap;
    protected final ConfigSetting<Boolean> autoCloak;
    protected final ConfigSetting<Integer> rememberEnemiesFor;
    protected final ConfigSetting<Boolean> stayAwayFromEnemies;
    protected final ConfigSetting<Boolean> ignoreContestedBoxes;

    public Box currentBox;
    protected long refreshing;

    protected long invisibleUntil, waitingUntil;

    public CollectorModule(PluginAPI api) {
        this(api, api.requireAPI(BotAPI.class),
                api.requireAPI(PetAPI.class),
                api.requireAPI(HeroAPI.class),
                api.requireAPI(StatsAPI.class),
                api.requireAPI(ConfigAPI.class),
                api.requireAPI(MovementAPI.class),
                api.requireAPI(HeroItemsAPI.class),
                api.requireAPI(EntitiesAPI.class),
                api.requireAPI(StarSystemAPI.class),
                api.requireInstance(SafetyFinder.class));
    }

    @Inject
    public CollectorModule(PluginAPI api,
                           BotAPI bot,
                           PetAPI pet,
                           HeroAPI hero,
                           StatsAPI stats,
                           ConfigAPI config,
                           MovementAPI movement,
                           HeroItemsAPI items,
                           EntitiesAPI entities,
                           StarSystemAPI starSystem,
                           SafetyFinder safetyFinder) {
        this.api = api;
        this.bot = bot;
        this.pet = pet;
        this.hero = hero;
        this.stats = stats;
        this.items = items;
        this.movement = movement;
        this.starSystem = starSystem;

        this.boxes = entities.getBoxes();
        this.players = entities.getPlayers();
        this.portals = entities.getPortals();

        this.safetyFinder = safetyFinder;

        this.workingMap = config.requireConfig("general.working_map");
        this.autoCloak = config.requireConfig("collect.auto_cloack");
        this.rememberEnemiesFor = config.requireConfig("general.running.remember_enemies_for");
        this.stayAwayFromEnemies = config.requireConfig("collect.stay_away_from_enemies");
        this.ignoreContestedBoxes = config.requireConfig("collect.ignore_contested_boxes");
    }

    @Override
    public boolean canRefresh() {
        return isNotWaiting();
    }

    @Override
    public String getStatus() {
        if (currentBox == null) return "Roaming";

        return !isNotWaiting() ?
                "Collecting " + currentBox.getTypeName() + " " + (waitingUntil - System.currentTimeMillis()) + "ms"
                : "Moving to " + currentBox.getTypeName();
    }

    @Override
    public void onTickModule() {
        if (isNotWaiting() && checkDangerousAndCurrentMap()) {
            hero.setRoamMode();
            pet.setEnabled(true);
            checkInvisibility();
            checkDangerous();

            findBox();

            if (!tryCollectNearestBox() && (hero.distanceTo(movement.getDestination()) < 20 || movement.isOutOfMap())) {
                movement.moveRandom();
            }
        }
    }

    public boolean isNotWaiting() {
        if (currentBox == null || !currentBox.isValid()) {
            waitingUntil = 0;
            return true;
        }

        return System.currentTimeMillis() > waitingUntil;
    }

    protected boolean checkDangerousAndCurrentMap() {
        safetyFinder.setRefreshing(System.currentTimeMillis() <= refreshing);
        return safetyFinder.tick() && checkMap();
    }

    protected GameMap getWorkingMap() {
        return starSystem.getOrCreateMapById(workingMap.getValue());
    }

    protected boolean checkMap() {
        GameMap map = getWorkingMap();
        if (!portals.isEmpty() && map != starSystem.getCurrentMap()) {
            this.bot.setModule(new MapModule(api, true)).setTarget(map);
            return false;
        }

        return true;
    }

    public void findBox() {
        Box best = boxes
                .stream()
                .filter(this::canCollect)
                .min(Comparator.<Box>comparingInt(b -> b.getInfo().getPriority())
                        .thenComparingDouble(hero::distanceTo)).orElse(null);

        this.currentBox = currentBox == null || best == null || currentBox.isCollected() || isBetter(best)
                ? best : currentBox;
    }

    protected boolean canCollect(Box box) {
        return box.getInfo().shouldCollect()
                && !box.isCollected()
                && movement.getClosestDistance(box) < 200
                && (!isResource(box.getTypeName()) || stats.getCargo() < stats.getMaxCargo())
                && !isContested(box);
    }

    protected boolean isResource(String type) {
        return type.equals("FROM_SHIP") || type.equals("PROSPEROUS_CARGO");
    }

    public boolean tryCollectNearestBox() {
        if (currentBox != null) {
            collectBox();
            return true;
        }

        return false;
    }

    protected void collectBox() {
        double distance = hero.distanceTo(currentBox);

        if (distance < 250) {
            movement.stop(false);
            if (!currentBox.tryCollect())
                return;
//            if (!hero.hasEffect(EntityEffect.BOX_COLLECTING) || hero.distanceTo(currentBox) == 0)
//                currentBox.tryCollect();
//            else return;

            waitingUntil = System.currentTimeMillis()
                    + currentBox.getInfo().getWaitTime()
                    + Math.min(1_000, currentBox.getRetries() * 100) // Add 100ms per retry, max 1 second
                    + hero.timeTo(distance) + 30;
        } else {
            movement.moveTo(currentBox);
        }
    }

    protected void checkDangerous() {
        if (stayAwayFromEnemies.getValue()) {
            Player dangerous = findClosestEnemyAndAddToDangerousList();
            if (dangerous != null) stayAwayFromLocation(dangerous);
        }
    }

    public void checkInvisibility() {
        if (autoCloak.getValue()
                && !hero.isInvisible()
                && System.currentTimeMillis() - invisibleUntil > 30_000) {

            items.useItem(SelectableItem.Cpu.CL04K, ItemFlag.POSITIVE_QUANTITY)
                    .ifSuccessful(r -> invisibleUntil = System.currentTimeMillis());
        }
    }

    protected void stayAwayFromLocation(Locatable awayLocation) {
        double angle = awayLocation.angleTo(hero);
        double moveDistance = hero.getSpeed();
        double distance = DISTANCE_FROM_DANGEROUS + 100;

        Location target = Location.of(awayLocation, angle, distance);
        moveDistance = moveDistance - target.distanceTo(hero);

        if (moveDistance > 0) {
            angle += moveDistance / 3000;
            target.setTo(awayLocation.getX() - cos(angle) * distance,
                    awayLocation.getY() - sin(angle) * distance);
        }

        movement.moveTo(target);
    }

    protected Player findClosestEnemyAndAddToDangerousList() {
        return players.stream()
                .filter(s -> s.getEntityInfo().isEnemy() && !s.isInvisible() && s.distanceTo(hero) < DISTANCE_FROM_DANGEROUS)
                .peek(s -> {
                    if (!s.isBlacklisted() && s.isAttacking(hero))
                        s.setBlacklisted(rememberEnemiesFor.getValue());
                })
                .min(Comparator.comparingDouble(player -> player.distanceTo(hero)))
                .orElse(null);
    }

    private boolean isBetter(Box box) {
        double currentDistance = currentBox.distanceTo(hero);
        return currentDistance > 100 && currentDistance - 150 > box.distanceTo(hero);
    }

    protected boolean isContested(Box box) {
        if (!ignoreContestedBoxes.getValue()) return false;

        // Pessimistic estimate: assume hero takes twice as long as others, cause speed is unstable.
        double heroTimeTo = hero.timeTo(box) * 2;
        return players.stream()
                .filter(ship -> ship.getDestination().isPresent())
                .filter(ship -> ship.getDestination().get().distanceTo(box) == 0)
                .anyMatch(ship -> heroTimeTo > ship.timeTo(box));
    }
}
