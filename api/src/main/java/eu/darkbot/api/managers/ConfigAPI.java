package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.legacy.Config;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * API providing access to the bot configuration
 *
 * Warning: this API is in a pretty experimental state,
 * and it may suffer significant changes once better defined
 */
public interface ConfigAPI extends API.Singleton {

    @Deprecated
    Config getConfig();

    /**
     * Get the configuration for a specific path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration on this path, null if not found
     */
    @Nullable <T> ConfigSetting<T> getConfig(String path);

    /**
     * Get the current value of the configuration in a specific path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration value on this path, null if not found
     * @throws ClassCastException if the type T does not properly match the type of this setting
     */
    default @Nullable <T> T getConfigValue(String path) {
        ConfigSetting<T> config = getConfig(path);
        return config != null ? config.getValue() : null;
    }

    /**
     * Returns all child nodes that stem from the provided path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @return set of child nodes available, null for leaf nodes
     */
    Set<String> getChildren(String path);

}
