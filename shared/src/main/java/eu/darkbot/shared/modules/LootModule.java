package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.types.NpcExtraFlag;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.entities.Ship;
import eu.darkbot.api.game.enums.EntityEffect;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Location;
import eu.darkbot.api.managers.*;
import eu.darkbot.shared.utils.MapTraveler;
import eu.darkbot.shared.utils.SafetyFinder;

import java.util.Collection;
import java.util.Comparator;

import static java.lang.Double.min;
import static java.lang.Math.random;

public class LootModule implements Module {

    private final BotAPI bot;
    private final PetAPI pet;
    private final HeroAPI hero;
    private final PluginAPI pluginApi;
    private final SafetyFinder safety;
    private final MovementAPI movement;
    private final AttackAPI attack;
    private final StarSystemAPI starSystem;

    private final Collection<? extends Npc> npcs;
    private final Collection<? extends Ship> ships;
    private final Collection<? extends Portal> portals;

    private final ConfigSetting<Integer> workingMap;
    private final ConfigSetting<Integer> npcDistanceIgnore;
    private final ConfigSetting<Integer> maxCircleIterations;
    private final ConfigSetting<Boolean> runConfigInCircle;
    private final ConfigSetting<Boolean> onlyKillPrefered;

    protected Npc target;
    
    protected boolean backwards = false;
    protected long refreshing;

    public LootModule(BotAPI bot,
                      PetAPI pet,
                      HeroAPI hero,
                      ConfigAPI config,
                      PluginAPI pluginApi,
                      SafetyFinder safety,
                      MovementAPI movement,
                      EntitiesAPI entities,
                      AttackAPI attack,
                      StarSystemAPI starSystem) {
        this.bot = bot;
        this.pet = pet;
        this.hero = hero;
        this.pluginApi = pluginApi;
        this.safety = safety;
        this.movement = movement;
        this.attack = attack;
        this.starSystem = starSystem;

        this.npcs = entities.getNpcs();
        this.ships = entities.getShips();
        this.portals = entities.getPortals();

        this.workingMap = config.requireConfig("general.working_map");
        this.npcDistanceIgnore = config.requireConfig("loot.npc_distance_ignore");
        this.maxCircleIterations = config.requireConfig("loot.max_circle_iterations");
        this.runConfigInCircle = config.requireConfig("loot.run_config_in_circle");
        this.onlyKillPrefered = config.requireConfig("general.roaming.only_kill_preferred");
    }

    @Override
    public void onTickModule() {
        if (checkDangerousAndCurrentMap()) {
            pet.setEnabled(true);

            if (findTarget()) {
                moveToAnSafePosition();
                ignoreInvalidTarget();
                attack.tryLockAndAttack();
            } else {
                hero.setRoamMode();
                if (!movement.isMoving()) movement.moveRandom();
            }
        }
    }

    @Override
    public boolean canRefresh() {
        if (!attack.hasTarget()) refreshing = System.currentTimeMillis() + 10000;
        return !attack.hasTarget() && safety.state() == SafetyFinder.Escaping.WAITING;
    }

    @Override
    public String getStatus() {
        return safety.state() != SafetyFinder.Escaping.NONE ? safety.status()
                : (attack.hasTarget() ? "Killing npc" : "Roaming");
    }

    protected boolean checkDangerousAndCurrentMap() {
        safety.setRefreshing(System.currentTimeMillis() <= refreshing);
        return safety.tick() && checkMap();
    }

    protected boolean checkMap() {
        if (!workingMap.getValue().equals(starSystem.getCurrentMap().getId()) && !portals.isEmpty()) {
            this.bot.setModule(new MapModule(bot, pluginApi.requireInstance(MapTraveler.class),
                            pluginApi.requireAPI(I18nAPI.class)))
                    .setTarget(starSystem.getOrCreateMapById(workingMap.getValue()));
            return false;
        }
        return true;
    }

    protected boolean findTarget() {
        attack.setTarget(target = closestNpc(hero));
        return attack.hasTarget();
    }

    protected void ignoreInvalidTarget() {
        double closestDist = movement.getClosestDistance(target);
        if (hero.getTarget() != target) {
            if (closestDist > 600) {
                target.setBlacklisted(1000);
                attack.setTarget(null);
            }
        } else if (!(target.getInfo().hasExtraFlag(NpcExtraFlag.IGNORE_OWNERSHIP) || target.isOwned())
                /*|| attack.isBugged()*/
                || (hero.distanceTo(target) > npcDistanceIgnore.getValue()) // Too far away from ship
                || (closestDist > 650 && target.getHealth().hpPercent() > 0.90)   // Too far into obstacle and full hp
                || (closestDist > 500 && !target.isMoving() // Inside obstacle, waiting & and regen shields
                && (target.getHealth().shieldIncreasedIn(1000) || target.getHealth().shieldPercent() > 0.99))) {
            target.setBlacklisted(5000);
            attack.setTarget(null);
        } else if (target.getEntityInfo().getUsername().contains("Invoke") && target.getInfo().hasExtraFlag(NpcExtraFlag.PASSIVE)
                && target == hero.getTarget()/* && !attack.castingAbility()*/) {
            target.setBlacklisted(600_000);
            attack.setTarget(null);
        }
    }

    protected void moveToAnSafePosition() {
        Location direction = movement.getDestination();
        Location targetLoc = target.getLocationInfo().destinationInTime(400);

        double distance = hero.distanceTo(target);
        double angle = targetLoc.angleTo(hero);
        double radius = target.getInfo().getRadius();
        boolean noCircle = target.getInfo().hasExtraFlag(NpcExtraFlag.NO_CIRCLE);

        radius = attack.modifyRadius(radius);
        if (radius > 750) noCircle = false;

        double angleDiff;
        if (noCircle) {
            double dist = targetLoc.distanceTo(direction);
            double minRad = Math.max(0, Math.min(radius - 200, radius * 0.5));
            if (dist <= radius && dist >= minRad) {
                setConfig(direction);
                return;
            }
            distance = minRad + random() * (radius - minRad - 10);
            angleDiff = (random() * 0.1) - 0.05;
        } else {
            double maxRadFix = target.getInfo().getRadius() / 2,
                    radiusFix = (int) Math.max(Math.min(radius - distance, maxRadFix), -maxRadFix);
            distance = (radius += radiusFix);
            // Moved distance + speed - distance to chosen radius same angle, divided by radius
            angleDiff = Math.max(/*(hero.shipInfo.speed * 0.625) +*/ (min(200, target.getLocationInfo().getSpeed()) * 0.625)
                    - hero.distanceTo(Location.of(targetLoc, angle, radius)), 0) / radius;
        }
        direction = getBestDir(targetLoc, angle, angleDiff, distance);

        while (!movement.canMove(direction) && distance < 10000)
            direction.toAngle(targetLoc, angle += backwards ? -0.3 : 0.3, distance += 2);
        if (distance >= 10000) direction.toAngle(targetLoc, angle, 500);

        setConfig(direction);

        movement.moveTo(direction);
    }

    protected Location getBestDir(Locatable targetLoc, double angle, double angleDiff, double distance) {
        int maxCircleIterations = this.maxCircleIterations.getValue(), iteration = 1;
        double forwardScore = 0, backScore = 0;
        do {
            forwardScore += score(Locatable.of(targetLoc, angle + (angleDiff * iteration), distance));
            backScore += score(Locatable.of(targetLoc, angle - (angleDiff * iteration), distance));
            // Toggle direction if either one of the directions is perfect, or one is 300 better.
            if (forwardScore < 0 != backScore < 0 || Math.abs(forwardScore - backScore) > 300) break;
        } while (iteration++ < maxCircleIterations);

        if (iteration <= maxCircleIterations) backwards = backScore > forwardScore;
        return Location.of(targetLoc, angle + angleDiff * (backwards ? -1 : 1), distance);
    }

    protected double score(Locatable loc) {
        return (movement.canMove(loc) ? 0 : -1000) - npcs.stream() // Consider barrier as bad as 1000 radius units.
                .filter(n -> target != n)
                .mapToDouble(n -> Math.max(0, n.getInfo().getRadius() - n.distanceTo(loc)))
                .sum();
    }

    protected void setConfig(Locatable direction) {
        if (!attack.hasTarget()) hero.setRoamMode();
        else if (runConfigInCircle.getValue()
                && target.getHealth().hpPercent() < 0.25
                && hero.distanceTo(direction) > target.getInfo().getRadius() * 2) hero.setRunMode();
        else if (hero.distanceTo(direction) > target.getInfo().getRadius() * 3) hero.setRoamMode();
        else hero.setAttackMode(target);
    }

    protected boolean isAttackedByOthers(Npc npc) {
        for (Ship ship : this.ships) {
            if (!ship.isAttacking(npc)) continue;

            if (!npc.getInfo().hasExtraFlag(NpcExtraFlag.IGNORE_ATTACKED)) npc.setBlacklisted(20_000);
            return true;
        }
        return false;
    }

    protected Npc closestNpc(Locatable location) {
        int extraPriority = attack.hasTarget() &&
                (hero.getTarget() == target || hero.distanceTo(target) < 600)
                ? 20 - (int) (target.getHealth().hpPercent() * 10) : 0;

        return this.npcs.stream()
                .filter(n -> (n == target && hero.isAttacking(target)) ||
                        ((!onlyKillPrefered.getValue() || movement.isInPreferredZone(n))
                                && shouldKill(n)
                                && movement.getClosestDistance(n) < 500))
                .min(Comparator.<Npc>comparingInt(n -> n.getInfo().getPriority() - (n == target ? extraPriority : 0))
                        .thenComparing(n -> n.getHealth().hpPercent())
                        .thenComparing(n -> n.distanceTo(location))).orElse(null);
    }

    protected boolean shouldKill(Npc n) {
        boolean attacked = this.isAttackedByOthers(n);
        return n.getInfo().shouldKill() && !n.isBlacklisted() &&
                (hero.hasEffect(EntityEffect.ENERGY_LEECH) || !n.getInfo().hasExtraFlag(NpcExtraFlag.LEECH_ONLY)) &&
                (n.getInfo().hasExtraFlag(NpcExtraFlag.IGNORE_ATTACKED) || !attacked) && // Either ignore attacked, or not being attacked
                (!n.getInfo().hasExtraFlag(NpcExtraFlag.ATTACK_SECOND) || attacked) &&   // Either don't want to attack second, or being attacked
                (n.getEntityInfo().getUsername().contains("Invoke")
                        || !n.getInfo().hasExtraFlag(NpcExtraFlag.PASSIVE) || n.isAttacking(hero));
    }
}
