package eu.darkbot.api.config;

import eu.darkbot.api.config.util.PercentRange;
import eu.darkbot.api.config.util.ShipMode;

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
        double getShieldToRepair();

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


    }
}
