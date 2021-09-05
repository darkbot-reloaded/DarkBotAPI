package eu.darkbot.shared.utils;

import eu.darkbot.api.config.legacy.General;
import eu.darkbot.api.config.types.SafetyInfo;
import eu.darkbot.api.events.EventHandler;
import eu.darkbot.api.events.Listener;
import eu.darkbot.api.game.entities.BattleStation;
import eu.darkbot.api.game.entities.Entity;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.entities.Ship;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Location;
import eu.darkbot.api.managers.AttackAPI;
import eu.darkbot.api.managers.ConfigAPI;
import eu.darkbot.api.managers.EntitiesAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import eu.darkbot.api.managers.MovementAPI;
import eu.darkbot.api.managers.StarSystemAPI;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SafetyFinder implements Listener {

    protected final HeroAPI hero;
    protected final AttackAPI attacker;
    protected final HeroItemsAPI items;
    protected final MovementAPI movement;
    protected final StarSystemAPI starSystem;

    protected final ConfigAPI config;
    protected final General.Safety SAFETY;
    protected final General.Running RUNNING;
    protected final Collection<? extends Ship> ships;

    protected final MapTraveler mapTraveler;
    protected final PortalJumper jumper;

    protected SafetyInfo safety;
    protected Escaping escape = Escaping.NONE;
    protected boolean refreshing;
    protected long escapingSince = -1;
    protected long lastTick;

    public enum Escaping {
        ENEMY, SIGHT, REPAIR, REFRESH, WAITING, NONE;
        boolean canUse(SafetyInfo safety) {
            if (safety.getType() == SafetyInfo.Type.CBS) {
                BattleStation cbs = ((BattleStation) safety.getEntity().orElse(null));
                if (cbs == null) return false;
                // Ignore enemy CBS, and if set to ALLY only, ignore empty meteorites (hull = 0)
                if (cbs.getEntityInfo().isEnemy() || (cbs.getHullId() == 0 && safety.getCbsMode() == SafetyInfo.CbsMode.ALLY)) return false;
            }
            return safety.getRunMode().ordinal() <= this.ordinal();
        }
        boolean shouldJump(SafetyInfo safety) {
            SafetyInfo.JumpMode jm = safety.getJumpMode();
            if (safety.getType() != SafetyInfo.Type.PORTAL || jm == null) return false;
            return jm.ordinal() > this.ordinal();
        }
    }

    protected JumpState jumpState = JumpState.CURRENT_MAP;
    protected enum JumpState {CURRENT_MAP, JUMPING, JUMPED, RETURNING, RETURNED}
    protected GameMap prevMap;

    public SafetyFinder(HeroAPI hero,
                        AttackAPI attacker,
                        HeroItemsAPI items,
                        MovementAPI movement,
                        StarSystemAPI starSystem,
                        ConfigAPI config,
                        EntitiesAPI entities,
                        MapTraveler mapTraveler,
                        PortalJumper portalJumper) {
        this.hero = hero;
        this.attacker = attacker;
        this.items = items;
        this.movement = movement;
        this.starSystem = starSystem;

        this.config = config;
        this.SAFETY = config.getLegacy().getGeneral().getSafety();
        this.RUNNING = config.getLegacy().getGeneral().getRunning();

        this.ships = entities.getShips();

        this.mapTraveler = mapTraveler;
        this.jumper = portalJumper;
    }

    @EventHandler
    protected void onMapChange(StarSystemAPI.MapChangeEvent event) {
        if (safety != null && safety.getType() == SafetyInfo.Type.PORTAL) {
            if (event.getNext() == prevMap) jumpState = JumpState.RETURNED;
            else if (jumpState == JumpState.JUMPING) jumpState = JumpState.JUMPED;
        }
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    public Escaping state() {
        return jumpState == JumpState.JUMPED && safety.getJumpMode() == SafetyInfo.JumpMode.ALWAYS_OTHER_SIDE ?
                Escaping.WAITING : escape;
    }

    public String status() {
        return "Escaping " + simplify(escape) + (safety == null ? "" : " " + safety +
                (safety.getType() == SafetyInfo.Type.PORTAL ? " " + simplify(jumpState) + (jumpState == JumpState.RETURNING ? " " + prevMap : "") : ""));
    }

    private String simplify(Object obj) {
        return obj.toString().toLowerCase().replace("_", " ");
    }

    /**
     * @return True if it's safe to keep working, false if the safety is working.
     */
    public boolean tick() {
        if (escape == Escaping.WAITING) {
            if (escapingSince == -1) escapingSince = System.currentTimeMillis();

            long escapeTime = System.currentTimeMillis() - escapingSince;
            if (escapeTime > 121_000) escapingSince = -1;
            if (escapeTime > 120_000) return false; // Over 2 min waiting? Try ticking module a bit to move.
        }
        // If no tick occurred for a while, means safety finder should have reset (Probably died)
        if (System.currentTimeMillis() - lastTick > 2500) {
            escape = Escaping.NONE;
            jumpState = JumpState.CURRENT_MAP;
        }
        lastTick = System.currentTimeMillis();

        if (jumpState == JumpState.CURRENT_MAP || jumpState == JumpState.JUMPING) {
            activeTick();

            if (escape == Escaping.NONE || safety == null) return true;

            if (hero.getLocationInfo().distanceTo(safety) > safety.getRadius()) {
                moveToSafety();
                hero.setRunMode();
                return false;
            }
        }

        switch (jumpState) {
            case CURRENT_MAP:
                if (escape.shouldJump(safety)
                        // Also jump if taking damage & you would jump away from enemy.
                        || (hero.getHealth().hpDecreasedIn(200) && Escaping.ENEMY.shouldJump(safety))) {
                    jumpState = JumpState.JUMPING;
                    movement.stop(false);
                }
                break;
            case JUMPING:
                prevMap = starSystem.getCurrentMap();
                safety.getEntity()
                        .ifPresent(e -> jumper.jump((Portal) e));
                return false;
            case JUMPED:
                if (hero.getHealth().hpDecreasedIn(100) || safety.getJumpMode() != SafetyInfo.JumpMode.ALWAYS_OTHER_SIDE
                        || (!refreshing && doneRepairing())) {
                    jumpState = JumpState.RETURNING;
                    mapTraveler.setTarget(prevMap);
                }
                break;
            case RETURNING:
                if (mapTraveler.isDone()) mapTraveler.setTarget(prevMap);
                mapTraveler.tick();
                return false;
        }

        if (jumpState == JumpState.CURRENT_MAP || jumpState == JumpState.RETURNED) {
            escape = Escaping.WAITING;

            if (!refreshing && doneRepairing() && !hasEnemy()) {
                escape = Escaping.NONE;
                jumpState = JumpState.CURRENT_MAP;
                return true;
            }
        }
        return false;
    }

    protected void activeTick() {
        Escaping oldEscape = escape;
        escape = getEscape();
        if (escape == Escaping.NONE || escape == Escaping.WAITING) return;

        if (jumpState == JumpState.CURRENT_MAP &&
                (escape != oldEscape || safety == null || safety.getEntity().map(e -> !e.isValid()).orElse(true))) {
            safety = getSafety();
        }
        if (safety == null) {
            escape = Escaping.NONE;
            return;
        }

        if (oldEscape != escape && escape == Escaping.ENEMY) castDefensiveAbility();
    }

    protected Escaping getEscape() {
        if (escape == Escaping.ENEMY || isUnderAttack()) return Escaping.ENEMY;
        if (escape == Escaping.WAITING) return Escaping.WAITING;
        if ((escape == Escaping.SIGHT && !RUNNING.getStopRunning()) || hasEnemy()) return Escaping.SIGHT;
        if (escape == Escaping.REPAIR || hero.getHealth().hpPercent() < SAFETY.getRepairHealthRange().getMin() ||
                (hero.getHealth().hpPercent() < this.SAFETY.getRepairHealthNoNpc() &&
                        (!attacker.hasTarget() ||
                                Objects.requireNonNull(attacker.getTarget()).getHealth().hpPercent() > 0.9)))
            return Escaping.REPAIR;
        return refreshing ? Escaping.REFRESH : Escaping.NONE;
    }

    protected SafetyInfo getSafety() {
        List<SafetyInfo> safeties = config.getLegacy()
                .getSafeties(starSystem.getCurrentMap())
                .stream()
                .filter(s -> s.getEntity().map(Entity::isValid).orElse(false))
                .filter(escape::canUse)
                .peek(s -> s.setDistance(Math.max(0, movement.getDistanceBetween(hero, s) - s.getRadius())))
                .sorted(Comparator.comparingDouble(SafetyInfo::getDistance))
                .collect(Collectors.toList());
        if (safeties.isEmpty()) return null;

        SafetyInfo best = safeties.get(0);

        if (escape == Escaping.REPAIR || escape == Escaping.REFRESH ||
                RUNNING.getRunClosestDistance() == 0 ||
                best.getDistance() < RUNNING.getRunClosestDistance()) return best;

        List<Ship> enemies = ships.stream().filter(this::runFrom).collect(Collectors.toList());

        return safeties.stream()
                .filter(s -> s.getDistance() < enemies.stream()
                        .mapToDouble(enemy -> movement.getDistanceBetween(enemy, s))
                        .min().orElse(Double.POSITIVE_INFINITY))
                .findFirst()
                .orElse(best);
    }

    protected void moveToSafety() {
        if ((jumpState != JumpState.CURRENT_MAP && jumpState != JumpState.JUMPING)
                || movement.getDestination().distanceTo(safety) < safety.getRadius()
                || !safety.getEntity().map(Entity::isValid).orElse(false)) return;

        double angle = safety.angleTo(hero) + Math.random() * 0.2 - 0.1;
        movement.moveTo(Location.of(safety, angle, -safety.getRadius() * (0.3 + (0.60 * Math.random())))); // 30%-90% radius
    }

    protected void castDefensiveAbility() {
        if (movement.getDistanceBetween(hero, movement.getDestination()) >= RUNNING.getShipAbilityMinDistance()) {
            items.useItem(RUNNING.getShipAbility());
        }
    }

    protected boolean doneRepairing() {
        if (!hero.isInMode(SAFETY.getRepairMode())
                && (hero.getHealth().hpIncreasedIn(1000) || hero.getHealth().hpPercent() == 1)
                && (hero.getHealth().shieldDecreasedIn(1000) || hero.getHealth().shieldPercent() == 0)) hero.setMode(SAFETY.getRepairMode());
        return this.hero.getHealth().shieldPercent() >= SAFETY.getRepairToShield() &&
                hero.setMode(SAFETY.getRepairMode()) && this.hero.getHealth().hpPercent() >= SAFETY.getRepairHealthRange().getMax();
    }

    protected boolean isUnderAttack() {
        if (!RUNNING.getRunFromEnemies() && !RUNNING.getRunInSight()) return false;
        return ships.stream().anyMatch(s -> s.getEntityInfo().isEnemy() && isAttackingOrBlacklisted(s));
    }

    protected boolean hasEnemy() {
        if (!RUNNING.getRunFromEnemies() && !RUNNING.getRunInSight()) return false;
        return ships.stream().anyMatch(this::runFrom);
    }

    protected boolean runFrom(Ship ship) {
        return ship.getEntityInfo().isEnemy() && (isAttackingOrBlacklisted(ship) ||
                (RUNNING.getRunInSight() && ship.distanceTo(hero) < RUNNING.getMaxSightDistance()));
    }

    protected boolean isAttackingOrBlacklisted(Ship ship) {
        if (ship.isAttacking(hero)) ship.setBlacklisted(RUNNING.getEnemyRemember().toMillis());
        return ship.isBlacklisted();
    }

}
