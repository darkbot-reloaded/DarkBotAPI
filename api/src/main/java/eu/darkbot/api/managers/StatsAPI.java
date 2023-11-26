package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.game.stats.Stats;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

/**
 * Provides access to base statistics, like ping, level, running time, current/earned money etc.
 */
public interface StatsAPI extends API.Singleton {

    /**
     * Get a stat by its key
     * @param key The stat key to search
     * @return the object representing the statistic's values
     */
    Stat getStat(Key key);

    /**
     * Shorthand for {@code getStat(key).getCurrent()}
     * @param key The stat key to search
     * @return the current value of the stat
     * @throws NullPointerException if key provided is not a registered stat
     */
    default double getStatValue(Key key) {
        return getStat(key).getCurrent();
    }

    /**
     * Registers a new statistic to the bot stat system
     * @param key the key to create the stat for
     * @return The newly created stat
     * @throws IllegalArgumentException if {@param key} is in the default (null) namespace (the bot).
     */
    Stat registerStat(Key key);

    /**
     * Write the new value for a statistic
     * @param key key to write the stat to
     * @param newValue the new value for this stat
     * @throws IllegalArgumentException if {@param key} is in the default (null) namespace (the bot).
     */
    void setStatValue(Key key, double newValue);

    /**
     * Will reset running time and earned values.
     */
    void resetStats();

    /**
     * Gets current ping.
     *
     * @return current ping in milliseconds.
     */
    default int getPing() {
        return (int) getStatValue(Stats.Bot.PING);
    }

    /**
     * @return current hero level, derived from experience
     */
    default int getLevel() {
        return Math.max(1, (int) (Math.log(getTotalExperience() / 10_000) / Math.log(2)) + 2);
    }

    /**
     * @return current amount of cargo
     */
    default int getCargo() {
        return (int) getStatValue(Stats.General.CARGO);
    }

    /**
     * @return max amount of cargo
     */
    default int getMaxCargo() {
        return (int) getStatValue(Stats.General.MAX_CARGO);
    }

    /**
     * @return running time in milliseconds.
     */
    default Duration getRunningTime() {
        return Duration.ofMillis((long) getStat(Stats.Bot.RUNTIME).getEarned());
    }

    /**
     * @return total amount of credits which hero currently have
     */
    default double getTotalCredits() {
        return getStatValue(Stats.General.CREDITS);
    }

    /**
     * @return earned amount of credits while bot was working
     */
    default double getEarnedCredits() {
        return getStat(Stats.General.CREDITS).getEarned();
    }

    /**
     * @return total amount of uridium which hero currently have
     */
    default double getTotalUridium() {
        return getStatValue(Stats.General.URIDIUM);
    }

    /**
     * @return earned amount of uridium while bot was working
     */
    default double getEarnedUridium() {
        return getStat(Stats.General.URIDIUM).getEarned();
    }

    /**
     * @return total amount of experience which hero currently have
     */
    default double getTotalExperience() {
        return getStatValue(Stats.General.EXPERIENCE);
    }

    /**
     * @return earned amount of experience while bot was working
     */
    default double getEarnedExperience() {
        return getStat(Stats.General.EXPERIENCE).getEarned();
    }

    /**
     * @return total amount of honor which hero currently have
     */
    default double getTotalHonor() {
        return getStatValue(Stats.General.HONOR);
    }

    /**
     * @return earned amount of honor while bot was working
     */
    default double getEarnedHonor() {
        return getStat(Stats.General.HONOR).getEarned();
    }

    /**
     * @return total amount of nova energy which hero currently have
     */
    default int getNovaEnergy() {
        return (int) getStatValue(Stats.General.NOVA_ENERGY);
    }

    /**
     * A stat's key is a unique identifier for the stat, which marks the namespace (plugin) for the stat,
     * the category (if any), and a name.
     */
    interface Key {
        /**
         * The namespace for the key, in other words, an id for the plugin that is responsible for this key
         * @return the namespace for the key: a base package if from a plugin, null if native
         */
        String namespace();

        /**
         * @return A category this stat is part of, for display purposes
         */
        String category();

        /**
         * @return The name of this specific statistic
         */
        String name();
    }

    /**
     * Represents a statistic being tracked.
     */
    interface Stat {
        /**
         * @return The first value obtained when tracking started
         */
        double getInitial();

        /**
         * @return The sum of all positive changes to the stat, while the bot was running
         */
        double getEarned();

        /**
         * @return The sum of all negative changes to the stat, while the bot was running
         */
        double getSpent();

        /**
         * @return The latest known value.
         */
        double getCurrent();

        /**
         * @return A time series with a history of data values, or null if time tracking isn't enabled
         */
        @Nullable TimeSeries getTimeSeries();
    }

    /**
     * Represents a historical time series, with point in time -> value correlations
     */
    interface TimeSeries {
        /**
         * @return a read-only list of timestamps (in millis) at which values are taken
         */
        List<Long> time();

        /**
         * @return a read-only list of values for the stat
         */
        List<Double> value();
    }

    class StatsResetEvent implements Event {
        public StatsResetEvent() {
        }
    }

}
