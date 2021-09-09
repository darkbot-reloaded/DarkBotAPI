package eu.darkbot.api.config;

import eu.darkbot.api.config.util.ValueHandler;
import eu.darkbot.api.extensions.PluginInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a setting that can be configured by the user
 */
public interface ConfigSetting<T> {

    /**
     * @return parent configuration setting
     */
    @Nullable Parent<?> getParent();

    /**
     * Plugin namespace this config setting belongs to.
     * Only the root node needs to provide a namespace, all children will default to that.
     * @return namespace used in i18n for translating the config
     */
    default @Nullable PluginInfo getNamespace() {
        Parent<?> parent = getParent();
        return parent == null ? null : parent.getNamespace();
    }

    /**
     * @return translation key for this setting.
     */
    String getKey();

    /**
     * @return user-readable name for this setting, obtained from translation files
     */
    String getName();

    /**
     * @return user-readable description for the setting, null if no description exists
     */
    @Nullable String getDescription();

    /**
     * @return class type of this config setting
     */
    Class<T> getType();

    /**
     * @return get the current value of this setting
     */
    T getValue();

    /**
     * @param value update the value of this setting
     */
    void setValue(T value);

    /**
     * Append a listener that will be called whenever the value is updated
     *
     * Listeners will only be weakly referenced, and as such, you are responsible
     * for keeping a strong reference to the listener to avoid it being removed.
     *
     * @param listener listener to be appended
     */
    void addListener(Consumer<T> listener);

    /**
     * Remove a listener that was previously added
     *
     * @param listener listener to remove
     */
    void removeListener(Consumer<T> listener);

    /**
     * @return the value handler for this config setting
     */
    ValueHandler<T> getHandler();

    /**
     * Config settings that are not leaf nodes of the tree, will implement the parent interface,
     * having a list of children available by key
     * @param <T> type of the parent config
     */
    interface Parent<T> extends ConfigSetting<T> {
        /**
         * @return map with the children mapped by a key
         */
        Map<String, ConfigSetting<?>> getChildren();
    }

}
