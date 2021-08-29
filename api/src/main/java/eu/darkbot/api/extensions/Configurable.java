package eu.darkbot.api.extensions;

import eu.darkbot.api.config.ConfigSetting;

/**
 * Marks this feature supporting user configuration.
 * The user configuration needs to be a custom java object that will be parsed for
 *
 *
 * @param <T> the type of configuration to use
 */
public interface Configurable<T> {
    void setConfig(ConfigSetting<T> config);
}
