package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.config.util.ShipMode;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.entities.Ship;
import eu.darkbot.api.game.items.SelectableItem;
import org.jetbrains.annotations.Nullable;

/**
 * This {@link API} represent hero entity,
 * from here you can manage your ship.
 */
public interface HeroAPI extends Ship, API.Singleton {

    /**
     * @return current used {@link Configuration}
     */
    Configuration getConfiguration();

    /**
     * @return currently used {@link Configuration}
     */
    SelectableItem.Formation getFormation();

    /**
     * Will check if {@link HeroAPI} is in given {@code mode}.
     *
     * A mode is the combination of a configuration and a formation
     *
     * @param mode the mode to check
     * @return true if {@link HeroAPI} is in given {@link ShipMode}
     */
    boolean isInMode(ShipMode mode);

    /**
     * Will check if {@link HeroAPI} is in the given {@link ShipMode},
     * if it isn't, it will try to set the {@link ShipMode}
     *
     * Keep in mind because of in-game cool-downs it can take a while
     * to apply the mode, you should keep on calling the function each
     * tick with the mode you want to keep set on your ship.
     *
     * Checking {@link #isInMode} beforehand is unadvised, simply call this directly.
     *
     * Unless you have user-defined modes in the config for your feature,
     * you'll probably find more use in one of the base modes:
     * @see #setAttackMode(Npc)
     * @see #setRoamMode()
     * @see #setRunMode()
     *
     * @param mode the flying mode to set
     * @return true if the ship is now flying in the given mode, false otherwise
     */
    boolean setMode(ShipMode mode);

    /**
     * Attempts to {@link #setMode} with the user-defined mode to attack this type of NPC.
     *
     * If no npc is selected you can use null for default attack configuration, however,
     * always prefer passing in the NPC for better user control over formations.
     *
     * @param target what Npc to configure attacking mode for
     * @return true if the ship is now flying in attack mode for this npc, false otherwise
     */
    boolean setAttackMode(@Nullable Npc target);

    /**
     * Attempts to {@link #setMode} with the user-defined mode to roam.
     * @return true if the ship is now flying in run mode, false otherwise
     */
    boolean setRoamMode();

    /**
     * Attempts to {@link #setMode} with the user-defined mode to run.
     * @return true if the ship is now flying in run mode, false otherwise
     */
    boolean setRunMode();

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
    }

}
