package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.types.NpcFlag;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.game.entities.Box;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.managers.ConfigAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.I18nAPI;
import eu.darkbot.api.managers.MovementAPI;
import eu.darkbot.api.managers.PetAPI;
import eu.darkbot.api.utils.Inject;

@Feature(name = "Kill & Collect", description = "Kills npcs and collects resources at the same time.")
public class LootCollectorModule implements Module {

    protected LootModule loot;
    protected CollectorModule collector;
    protected final PetAPI pet;
    protected final HeroAPI hero;
    protected final MovementAPI movement;
    protected final I18nAPI i18n;

    protected final ConfigSetting<Integer> collectRadius;

    public LootCollectorModule(PluginAPI api) {
        this(api.requireInstance(LootModule.class),
                api.requireInstance(CollectorModule.class),
                api.requireAPI(ConfigAPI.class),
                api.requireAPI(I18nAPI.class));
    }

    @Inject
    public LootCollectorModule(LootModule loot,
                               CollectorModule collector,
                               ConfigAPI config,
                               I18nAPI i18n) {
        this.loot = loot;
        this.collector = collector;
        this.pet = loot.pet;
        this.hero = loot.hero;
        this.movement = loot.movement;
        this.i18n = i18n;

        this.collectRadius = config.requireConfig("collect.radius");
    }

    @Override
    public void onTickModule() {
        if (collector.isNotWaiting() && loot.checkDangerousAndCurrentMap()) {
            pet.setEnabled(true);

            if (loot.findTarget()) {
                collector.findBox();

                Box box = collector.currentBox;
                Npc npc = loot.getAttacker().getTargetAs(Npc.class);

                if (box == null || !box.isValid()
                        || box.distanceTo(hero) > collectRadius.getValue()
                        || (npc.getInfo().hasExtraFlag(NpcFlag.IGNORE_BOXES)
                        && npc.distanceTo(box) > Math.min(800, npc.getInfo().getRadius() * 2))
                        || npc.getHealth().hpPercent() < 0.25) {
                    loot.moveToAnSafePosition();
                } else {
                    loot.setConfig(box);
                    collector.tryCollectNearestBox();
                }

                loot.ignoreInvalidTarget();
                loot.attack.tryLockAndAttack();

            } else {
                hero.setRoamMode();
                collector.findBox();

                if (!collector.tryCollectNearestBox() && (!movement.isMoving() || movement.isOutOfMap())) {
                    movement.moveRandom();
                }
            }
        }
    }

    @Override
    public boolean canRefresh() {
        return collector.isNotWaiting() && loot.canRefresh();

    }

    @Override
    public String getStatus() {
        return i18n.get("module.kill_n_collect.status", loot.getStatus(), collector.getStatus());
    }
}
