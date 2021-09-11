package eu.darkbot.impl.managers;

import eu.darkbot.api.extensions.AmmoProvider;
import eu.darkbot.api.game.other.Attackable;
import eu.darkbot.api.managers.AttackAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAttackImpl implements AttackAPI {

    protected final HeroItemsAPI heroItems;
    protected final HeroAPI hero;

    protected Attackable target;
    protected long lockTryTime, attackTryTime;
    protected int delayBetweenAttackAttempts = 750;

    private AbstractAttackImpl(HeroItemsAPI heroItems, HeroAPI hero) {
        this.heroItems = heroItems;
        this.hero = hero;
    }

    @Override
    public @Nullable Attackable getTarget() {
        return target;
    }

    @Override
    public void setTarget(@Nullable Attackable attackable) {
        this.target = attackable;
    }

    @Override
    public boolean isLocked() {
        return hasTarget() && hero.getTarget() == target;
    }

    @Override
    public void tryLockTarget() {
        if (isLocked() || lockTryTime > System.currentTimeMillis()) return;

        this.lockTryTime = System.currentTimeMillis() + (this.target.trySelect(false) ? 500 : 250);
    }

    @Override
    public boolean isAttacking() {
        return hasTarget() && hero.isAttacking(target);
    }

    @Override
    public void tryLockAndAttack() {
        if (isLocked()) {
            if (!hero.isAttacking() && attackTryTime > System.currentTimeMillis() - delayBetweenAttackAttempts) {
                attackTryTime = System.currentTimeMillis();

                AmmoProvider ammo = getBestAmmoProvider();
                if (ammo == null || ammo.getLaser() == null) return; //should only attack if laser ammo is not null?

                if (hero.getLaser() != ammo.getLaser()) {
                    heroItems.useItem(ammo.getLaser(), 500);

                    if (!isAttackViaSlotBarEnabled())
                        hero.triggerLaserAttack();
                }
            }
        } else tryLockTarget();
    }

    @Override
    public void stopAttack() {
        if (!hero.isAttacking() || attackTryTime > System.currentTimeMillis() - delayBetweenAttackAttempts) return;
        if (hero.triggerLaserAttack()) attackTryTime = System.currentTimeMillis();
    }

    @Override
    public double modifyRadius(double radius) {
        return radius;
    }

    protected abstract boolean isAttackViaSlotBarEnabled();

    protected abstract AmmoProvider getBestAmmoProvider();
}
