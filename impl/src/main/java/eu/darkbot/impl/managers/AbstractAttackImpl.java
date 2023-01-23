package eu.darkbot.impl.managers;

import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.game.other.Lockable;
import eu.darkbot.api.managers.AttackAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import eu.darkbot.util.Timer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractAttackImpl implements AttackAPI {

    /** Minimum delay between attack attempts in milliseconds */
    protected static final int ATTACK_DELAY = 500;

    protected final HeroItemsAPI heroItems;
    protected final HeroAPI hero;

    protected Lockable target;

    protected Timer lockTry = Timer.get(500);
    protected Timer attackTry = Timer.get(ATTACK_DELAY);

    protected boolean attacked;

    protected AbstractAttackImpl(HeroItemsAPI heroItems, HeroAPI hero) {
        this.heroItems = heroItems;
        this.hero = hero;
    }

    @Override
    public @Nullable Lockable getTarget() {
        return target;
    }

    @Override
    public void setTarget(@Nullable Lockable target) {
        if (Objects.equals(this.target, target)) return;
        this.target = target;
        lockTry.disarm();
        attackTry.disarm();
    }

    @Override
    public boolean isLocked() {
        return hasTarget() && Objects.equals(hero.getTarget(), target);
    }

    @Override
    public void tryLockTarget() {
        if (!hasTarget() || isLocked()) return;

        if (lockTry.tryActivate() && target.trySelect(false)) {
            hero.setLocalTarget(target);
            attacked = false;
        }
    }

    @Override
    public boolean isAttacking() {
        return hasTarget() && hero.isAttacking(target);
    }

    @Override
    public void tryLockAndAttack() {
        if (isLocked()) {
            SelectableItem.Laser laser = getBestLaserAmmo();

            if (laser != null
                    && hero.getLaser() != laser
                    && heroItems.useItem(laser, 500).isSuccessful()
                    && isAttackViaSlotBarEnabled()) {
                attackTry.activate();
                attacked = true;
                return;
            }

            // Always try to attack valid target, even with unknown ammo
            attack();
        } else {
            tryLockTarget();
        }
    }

    protected void attack() {
        if (isAttacking() || attacked && attackTry.isActive()) return;
        if (hero.triggerLaserAttack()) {
            attackTry.activate();
            attacked = true;
        }
    }

    @Override
    public void stopAttack() {
        if (!isAttacking() || attackTry.isActive()) return;
        if (hero.triggerLaserAttack()) attackTry.activate();
    }

    @Override
    public double modifyRadius(double radius) {
        return radius;
    }

    protected abstract boolean isAttackViaSlotBarEnabled();

    protected abstract SelectableItem.Laser getBestLaserAmmo();
}
