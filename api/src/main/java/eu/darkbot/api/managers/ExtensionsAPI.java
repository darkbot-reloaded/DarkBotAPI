package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import eu.darkbot.api.extensions.FeatureInfo;
import eu.darkbot.api.extensions.PluginInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides a way to inspect &amp; interact with plugins &amp; features loaded on the bot.
 * <p>
 * Generally this API shouldn't be of much use except when requiring one
 * feature to have a dependency on another.
 */
public interface ExtensionsAPI extends API.Singleton {

    /**
     * @return {@link Collection} of all available and loaded plugins.
     */
    @NotNull Collection<? extends PluginInfo> getPluginInfos();

    /**
     * @param feature class to get instance of
     * @param <T>     type of feature
     * @return instance of given feature
     */
    <T> Optional<T> getFeature(@NotNull Class<T> feature);

    /**
     * @param feature class to get {@link FeatureInfo} of
     * @param <T>     type of feature
     * @return {@link FeatureInfo} of given feature.
     */
    @Nullable <T> FeatureInfo<T> getFeatureInfo(@NotNull Class<T> feature);

    /**
     * If you have the actual class, prefer the above method.
     * Use this only to reference features that you can't have as a dependency to use the actual class.
     *
     * @param featureId canonical name of the class for the feature
     * @return {@link FeatureInfo} of given feature.
     */
    @Nullable FeatureInfo<?> getFeatureInfo(@NotNull String featureId);

    /**
     * Get what class loader has been used to load the plugin
     * May be used to search for resources in the plugin
     *
     * @param plugin plugin to get class loader of
     * @return the class loader used to load this plugin
     */
    ClassLoader getClassLoader(@NotNull PluginInfo plugin);

    /**
     * Plugin reloading stage
     */
    enum PluginStage {
        /**
         * Right after all plugins have been unloaded, should release any references
         * plugins in order to let them fully GC
         */
        BEFORE_LOAD,
        /**
         * Right after all plugins have initially loaded, but before
         * features have been sorted out and loaded
         */
        AFTER_LOAD,
        /**
         * The plugins and features are all ready-to-go
         */
        AFTER_LOAD_COMPLETE,
        /**
         * Same as {@link PluginStage#AFTER_LOAD_COMPLETE}, but called from
         * the UI thread to update components without messing up Swing's threading model
         */
        AFTER_LOAD_COMPLETE_UI
    }

    /**
     * Event launched whenever plugin state changes, this will occur in several
     * moments when plugins are being reloaded, or loaded for the first time.
     */
    class PluginLifetimeEvent implements Event {

        private final PluginStage stage;

        /**
         * @return the stage this event represents in plugin reloading
         */
        public PluginStage getStage() {
            return stage;
        }

        public PluginLifetimeEvent(PluginStage stage) {
            this.stage = stage;
        }
    }

}
