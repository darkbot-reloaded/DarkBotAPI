package eu.darkbot.api.extensions;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.managers.EventBrokerAPI;

/**
 * Interface that allows your {@link Feature} to setup and tear-down
 */
public interface Installable {

    /**
     * Called right after the feature is instanced, but also called
     * for {@link Module}s when they're selected by the user to start functioning.
     * <br>
     * Calls to the function should be independent when the plugin API provided is the same.
     *
     * @param pluginAPI the api to set up for
     */
    void install(PluginAPI pluginAPI);

    /**
     * Called right before the feature is unloaded from the bot completely, can occur when reloading plugins.
     * <br>
     * If using the {@link EventBrokerAPI} make sure to unregister your listener here.
     */
    default void uninstall() {}
}
