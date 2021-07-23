package eu.darkbot.api.managers;

import eu.darkbot.api.config.Config;

/**
 * API providing access to the bot configuration
 *
 * Warning: this API is in a pretty experimental state,
 * and it may suffer significant changes once better defined
 */
public interface ConfigAPI {

    @Deprecated
    Config getConfig();

    /**
     * Get the current configuration in a specific path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration value on this path, or null if non existant
     */
    <T> T getConfig(String path);

    /**
     * Returns all child nodes that stem from the provided path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @return list of child nodes available, null for leaf nodes
     */
    String[] getChildren(String path);

}
