package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.legacy.Config;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * API providing access to the bot configuration
 */
public interface ConfigAPI extends API.Singleton {

    @Deprecated
    default Config getConfig() {
        ConfigSetting<Config> cfg = getConfigRoot();
        return cfg.getValue();
    }

    /**
     * @param <T> the type to get it as
     * @return the root node of the bot configuration tree
     */
    <T> ConfigSetting<T> getConfigRoot();

    /**
     * Get the configuration for a specific path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration on this path, null if not found
     */
    default @Nullable <T> ConfigSetting<T> getConfig(String path) {
        return getConfig(getConfigRoot(), path);
    }

    /**
     * Get the current value of the configuration in a specific path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration value on this path, null if not found
     * @throws ClassCastException if the type T does not properly match the type of this setting
     */
    default @Nullable <T> T getConfigValue(String path) {
        return getConfigValue(getConfigRoot(), path);
    }

    /**
     * Returns all child nodes that stem from the provided path
     *
     * @param path configuration tree path, each segment is separated by a dot
     * @return set of child nodes available, null for leaf nodes
     */
    default Set<String> getChildren(String path) {
        return getChildren(getConfigRoot(), path);
    }

    /**
     * Get the configuration for a specific path, for a specific config
     *
     * @param root configuration root to search on
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration on this path, null if not found
     */
    default <T> ConfigSetting<T> getConfig(ConfigSetting<?> root, String path) {
        String[] paths = path.isEmpty() ? new String[]{} : path.split("\\.");
        for (String s : paths) {
            if (root instanceof ConfigSetting.Parent)
                root = ((ConfigSetting.Parent<?>) root).getChildren().get(s);
            else
                root = null;

            if (root == null)
                throw new IllegalArgumentException("Configuration not found: " + s + " in " + path);
        }
        return (ConfigSetting<T>) root;
    }

    /**
     * Get the current value of the configuration in a specific path, for a specific config
     *
     * @param root configuration root to search on
     * @param path configuration tree path, each segment is separated by a dot
     * @param <T> the type of the config node
     * @return the configuration value on this path, null if not found
     * @throws ClassCastException if the type T does not properly match the type of this setting
     */
    default <T> T getConfigValue(ConfigSetting<?> root, String path) {
        ConfigSetting<T> config = getConfig(root, path);
        return config != null ? config.getValue() : null;
    }

    /**
     * Returns all child nodes that stem from the provided path, for a specific config
     *
     * @param root configuration root to search on
     * @param path configuration tree path, each segment is separated by a dot
     * @return set of child nodes available, null for leaf nodes
     */
    default Set<String> getChildren(ConfigSetting<?> root, String path) {
        ConfigSetting<?> setting = getConfig(root, path);
        if (setting instanceof ConfigSetting.Parent)
            return ((ConfigSetting.Parent<?>) setting).getChildren().keySet();
        return null;
    }

}
