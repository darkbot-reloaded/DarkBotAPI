package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.extensions.TemporalModule;
import eu.darkbot.api.utils.Version;
import org.jetbrains.annotations.Nullable;

/**
 * Provides access bot management, like changing the module or toggling on &amp; off the bot.
 */
public interface BotAPI extends API.Singleton {

    /**
     * {@link Version} of the DarkBot.
     * @return The current version of the bot
     */
    Version getVersion();

    /**
     * @return avg time of tick in ms.
     */
    double getTickTime();

    /**
     * @return the bot is running, isn't stopped.
     */
    boolean isRunning();

    /**
     * You can play/stop the bot.
     * On stopped the {@link Module#onTickStopped()}, {@link Behavior#onStoppedBehavior()} will be ticked.
     * {@link Module#onTickModule()}, {@link Behavior#onTickBehavior()} otherwise.
     *
     * @param running should bot be running
     */
    void setRunning(boolean running);

    /**
     * @return current used {@link Module}
     */
    Module getModule();

    /**
     * Returns the currently used module if it's not a {@link eu.darkbot.api.extensions.TemporalModule},
     * otherwise returns the TemporalModule's previous module.
     * @return current used non-temporal {@link Module}
     */
    default Module getNonTemporalModule() {
        Module module = getModule();
        if (module instanceof TemporalModule) return ((TemporalModule) module).getBack();
        return module;
    }

    /**
     * Sets the currently running module in the bot.
     * Keep in mind that any pause &amp; restart will wipe this module and re-set the user defined module.
     * <br>
     * This is mainly useful to install {@link eu.darkbot.api.extensions.TemporalModule}s that
     * will take over the control of the bot for a small amount of time before delegating back
     * to the {@link Module} set by the user.
     * <br>
     * Examples:
     *  - A normal module may set a map traveling module to go to the working map
     *  - A behavior wanting temporal control over movement can install a temporal module that does that
     *
     * @param module The module to set, often a {@link eu.darkbot.api.extensions.TemporalModule}
     * @param <M> type of module
     * @return The same module that was passed in, useful to chain methods.
     */
    <M extends Module> M setModule(@Nullable M module);

    /**
     * Trigger a bot reload
     */
    void handleRefresh();

    /**
     * Event fired when running value changed
     */
    class RunningToggleEvent implements Event {
        private final boolean running;

        public RunningToggleEvent(boolean running) {
            this.running = running;
        }

        public boolean isRunning() {
            return running;
        }
    }
}
