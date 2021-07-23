package eu.darkbot.api.config;

import eu.darkbot.api.config.util.PercentRange;
import eu.darkbot.api.config.util.ShipMode;
import eu.darkbot.api.game.items.SelectableItem;

import java.time.Duration;

public interface General {

    /**
     * @return configuration for how to repair and use safeties
     */
    Safety getSafety();

    /**
     * @return configuration for when to run from enemies
     */
    Running getRunning();


    interface Safety {
        /**
         * @return A range of health to keep, if under min should repair, if above max should stop repairing
         */
        PercentRange getRepairHealthRange();

        /**
         * @return health percentage at which to start repairing if not attacking any npc
         */
        double getRepairHealthNoNpc();

        /**
         * @return shield percentage required to stop repairing
         */
        double getRepairToShield();

        /**
         * @return {@link ShipMode} to use while repairing
         */
        ShipMode getRepairMode();

    }

    interface Running {
        /**
         * @return true if the bot should run from enemies, false otherwise
         */
        boolean getRunFromEnemies();

        /**
         * @return how long to remember enemies that attacked the hero
         */
        Duration getEnemyRemember();

        /**
         * @return true to run if any enemy appears
         */
        boolean getRunInSight();

        /**
         * @return true if it should stop running when out of sight, false to always run until you reach safety
         */
        boolean getStopRunning();

        /**
         * @return maximum distance to consider enemies in, if further away they are ignored
         */
        int getMaxSightDistance();

        /**
         * @return the distance for which you should run to closest portal.
         *         If you're further away than this distance, run to the portal furthest from the enemy instead
         */
        int getRunClosestDistance();

        /**
         * @return minimum distance to safety for the ship ability to be used
         */
        int getShipAbilityMinDistance();

        /**
         * @return an item to trigger when running away
         */
        SelectableItem getShipAbility();

    }
}
