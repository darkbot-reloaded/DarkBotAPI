package eu.darkbot.api.extensions;

/**
 * A background running task that will run in the background thread.
 * <p>
 * There can be any amount of tasks running side by side on the bot, they
 * all share the same thread to make network lookups or back page actions
 * <p>
 * Some examples are things like:
 * <ul>
 * <li>Changing ship equipment
 * <li>Buying items
 * <li>Buying items in the auction
 * <li>Spinning galaxy gates
 * </ul>
 * <p>
 * Keep in mind since this does NOT run on the main thread, threading inconsistencies
 * may occur if you're referencing data from the main thread. Make sure to avoid
 * concurrency issues by doing atomic operations on data that is owned and
 * modified or mutated by the main thread.
 * <p>
 * If you're mixing main &amp; background thread logic, consider implementing both {@link Behavior}
 * and {@link Task}, and doing the main thread logic in one method and the background
 * logic in the other, and handle all synchronization in the feature directly.
 */
public interface Task {

    /**
     * This method will be called every background bot tick, how often this
     * is called can largely vary, from 100ms to minutes, depending on what
     * other background tasks are doing and how long their calls take.
     * <p>
     * As a general rule of thumb try not to hold the method for more than
     * a few seconds at the time, you can split your workload in different
     * calls of the function, so you do a bit each time the function is
     * called, that way you let other tasks get a hold of it too.
     * <p>
     * For example, a GG spinner may want to execute 10 quick spins in a
     * couple seconds, and then return to let other tasks run.
     */
    void onTickTask();

    /**
     * This method will be called every background bot tick, how often this
     * is called can largely vary, from 100ms to minutes, depending on what
     * other background tasks are doing and how long their calls take.
     * <p>
     * As a general rule of thumb try not to hold the method for more than
     * a few seconds at the time, you can split your workload in different
     * calls of the function, so you do a bit each time the function is
     * called, that way you let other tasks get a hold of it too.
     * <p>
     *
     * <h2>The main differences between {@link #onTickTask()} are:</h2>
     * <ul>
     *   <li>Doesn't wait for account to log-in, so can be called almost immediately after plugin load</li>
     *   <li>Doesn't wait for valid SID or server instance</li>
     *   <li>Should be used for request that aren't for the game backpage</li>
     * </ul>
     */
    default void onBackgroundTick() {}
}
