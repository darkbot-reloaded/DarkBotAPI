package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.config.types.ShipMode;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.entities.Player;
import eu.darkbot.api.game.items.Item;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Lockable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the in-game hero (your ship), provides methods to manage
 * hero mode (attacking, running) &amp; change formations or configuration.
 */
public interface HeroAPI extends Player, API.Singleton {

    /**
     * Get the current map the hero is in.
     * This method is equivalent to {@link StarSystemAPI#getCurrentMap()}
     *
     * @return current {@link GameMap} the hero is in
     */
    GameMap getMap();

    /**
     * Get the current local target the bot is using
     *
     * The local target represents an entity that the bot current module is
     * trying to, or successfully, targeting and attacking.
     */
    @Nullable Lockable getLocalTarget();

    default <T extends Lockable> @Nullable T getLocalTargetAs(Class<T> type) {
        Lockable target = getLocalTarget();
        return type.isInstance(target) ? type.cast(target) : null;
    }

    /**
     * @param target the local target to use
     * @see #getLocalTarget
     */
    void setLocalTarget(@Nullable Lockable target);

    /**
     * @return current used {@link Configuration}
     */
    Configuration getConfiguration();

    /**
     * Will check if {@link HeroAPI} is in given {@code mode}.
     * <p>
     * A mode is the combination of a configuration and a formation
     *
     * @param mode the mode to check
     * @return true if {@link HeroAPI} is in given {@link ShipMode}
     */
    boolean isInMode(@NotNull ShipMode mode);

    /**
     * Will check if {@link HeroAPI} is in the given {@link ShipMode},
     * if it isn't, it will try to set the {@link ShipMode}
     * <p>
     * Keep in mind because of in-game cool-downs it can take a while
     * to apply the mode, you should keep on calling the function each
     * tick with the mode you want to keep set on your ship.
     * <p>
     * Checking {@link #isInMode} beforehand is unadvised, simply call this directly.
     * <p>
     * Unless you have user-defined modes in the config for your feature,
     * you'll probably find more use in one of the base modes:
     *
     * @param mode the flying mode to set
     * @return true if the ship is now flying in the given mode, false otherwise
     * @see #setAttackMode(Npc)
     * @see #setRoamMode()
     * @see #setRunMode()
     * @see eu.darkbot.api.extensions.selectors.ShipModeSelector TODO
     */
    boolean setMode(@NotNull ShipMode mode);

    /**
     * Attempts to {@link #setMode} with the user-defined mode to attack this type of NPC.
     * <p>
     * If no npc is selected you can use null for default attack configuration, however,
     * always prefer passing in the NPC for better user control over formations.
     *
     * @param target what Npc to configure attacking mode for
     * @return true if the ship is now flying in attack mode for this npc, false otherwise
     * @see eu.darkbot.api.extensions.selectors.ShipModeSelector TODO
     */
    boolean setAttackMode(@Nullable Npc target);

    /**
     * Attempts to {@link #setMode} with the user-defined mode to roam.
     *
     * @return true if the ship is now flying in run mode, false otherwise
     * @see eu.darkbot.api.extensions.selectors.ShipModeSelector TODO
     */
    boolean setRoamMode();

    /**
     * Attempts to {@link #setMode} with the user-defined mode to run.
     *
     * @return true if the ship is now flying in run mode, false otherwise
     * @see eu.darkbot.api.extensions.selectors.ShipModeSelector TODO
     */
    boolean setRunMode();

    /**
     * Via this method you can trigger(stop/start) the laser attack.
     * Will attempt to trigger laser attack on whatever the target is.
     *
     * <b>Keep in mind that, update of {@link #isAttacking()} may take some ticks</b>
     *
     * @return if any trigger action of lasers (attack/abort) was successful
     * @see #getTarget()
     * @see #isAttacking()
     */
    boolean triggerLaserAttack();

    /**
     * Will try to launch the rocket which is currently selected
     *
     * @return if launch was successful
     * @see Item#isReady()
     */
    boolean launchRocket();

    /**
     * @return currently selected in-game {@link SelectableItem.Laser}
     */
    SelectableItem.Laser getLaser();

    /**
     * @return currently selected in-game {@link SelectableItem.Rocket}
     */
    SelectableItem.Rocket getRocket();

    /**
     * Represents in-game {@link HeroAPI} configs.
     */
    enum Configuration {
        UNKNOWN,
        FIRST,
        SECOND;

        public static Configuration of(int config) {
            return config == 1 ? FIRST :
                    config == 2 ? SECOND : UNKNOWN;
        }

        @Override
        public String toString() {
            return ordinal() + "C";
        }
    }
}
