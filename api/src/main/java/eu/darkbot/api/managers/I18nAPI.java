package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.extensions.PluginInfo;
import eu.darkbot.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides access to bot built-in translations, as well as plugin translations.
 * <p>
 * Access to bot base translations is available in methods without a namespace parameter:
 * <br>
 * {@link #get(String)}, {@link #get(String, Object...)},
 * {@link #getOrDefault(String, String)} or {@link #getOrDefault(String, String, Object...)},
 * Access to plugin translations is available in methods with a namespace parameter:
 * <br>
 * {@link #get(PluginInfo, String)}, {@link #get(PluginInfo, String, Object...)},
 * {@link #getOrDefault(PluginInfo, String, String)}, {@link #getOrDefault(PluginInfo, String, String, Object...)}
 */
public interface I18nAPI extends API.Singleton {

    /**
     * Get the translation for a specific bot translation key
     *
     * @param key translation key to search for
     * @return The translated text if available, a placeholder text otherwise
     */
    default String get(@NotNull String key) {
        return get(key, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the translation for a specific plugin translation key
     *
     * @param namespace the plugin namespace to search
     * @param key       translation key to search for
     * @return The translated text if available, a placeholder text otherwise
     */
    default String get(@Nullable PluginInfo namespace, @NotNull String key) {
        return get(namespace, key, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the translation for a specific bot translation key with arguments
     *
     * @param key       translation key to search for
     * @param arguments the arguments to put in the translated text
     * @return The translated text if available, a placeholder text otherwise
     */
    default String get(@NotNull String key, @NotNull Object... arguments) {
        return get(null, key, arguments);
    }

    /**
     * Get the translation for a specific plugin translation key with arguments
     *
     * @param namespace the plugin namespace to search
     * @param key       translation key to search for
     * @param arguments the arguments to put in the translated text
     * @return The translated text if available, a placeholder text otherwise
     */
    String get(@Nullable PluginInfo namespace, @NotNull String key, @NotNull Object... arguments);

    /**
     * Get the translation for a specific bot translation key, or fallback to a default string
     *
     * @param key      translation key to search for, if null, fallback string will be used
     * @param fallback fallback string, returned in case no translation was found
     * @return the translated message if available, fallback otherwise
     */
    default String getOrDefault(@Nullable String key, @Nullable String fallback) {
        return getOrDefault(key, fallback, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the translation for a specific plugin translation key, or fallback to a default string
     *
     * @param namespace the plugin namespace to search
     * @param key       translation key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as translation text and filled with arguments if no text is found
     * @return the translated message if available, fallback otherwise
     */
    default String getOrDefault(@Nullable PluginInfo namespace, @Nullable String key, @Nullable String fallback) {
        return getOrDefault(namespace, key, fallback, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the translation for a specific bot translation key, or fallback to a default string
     *
     * @param key       translation key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as translation text and filled with arguments if no text is found
     * @param arguments the arguments to put in the translated text (or in fallback if no translation is found)
     * @return the translated message if available, fallback otherwise
     */
    default String getOrDefault(@Nullable String key, @Nullable String fallback, @NotNull Object... arguments) {
        return getOrDefault(null, key, fallback, arguments);
    }

    /**
     * Get the translation for a specific bot translation key, or fallback to a default string
     *
     * @param namespace the plugin namespace to search
     * @param key       translation key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as translation text and filled with arguments if no text is found
     * @param arguments the arguments to put in the translated text (or in fallback if no translation is found)
     * @return the translated message if available, fallback otherwise
     */
    String getOrDefault(@Nullable PluginInfo namespace, @Nullable String key, @Nullable String fallback, @NotNull Object... arguments);

    /**
     * Get the description for a specific bot description key
     *
     * @param key description key to search for
     * @return The description text if available, a placeholder text otherwise
     */
    default String getDescription(@NotNull String key) {
        return getDescription(key, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the description for a specific plugin description key
     *
     * @param namespace the plugin namespace to search
     * @param key       description key to search for
     * @return The description text if available, a placeholder text otherwise
     */
    default String getDescription(@Nullable PluginInfo namespace, @NotNull String key) {
        return getDescription(namespace, key, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the description for a specific bot description key with arguments
     *
     * @param key       description key to search for
     * @param arguments the arguments to put in the description text
     * @return The description text if available, a placeholder text otherwise
     */
    default String getDescription(@NotNull String key, @NotNull Object... arguments) {
        return getDescription(null, key, arguments);
    }

    /**
     * Get the description for a specific plugin description key with arguments
     *
     * @param namespace the plugin namespace to search
     * @param key       description key to search for
     * @param arguments the arguments to put in the description text
     * @return The description text if available, a placeholder text otherwise
     */
    String getDescription(@Nullable PluginInfo namespace, @NotNull String key, @NotNull Object... arguments);


    /**
     * Get the description for a specific bot description key, or fallback to a default string
     *
     * @param key      description key to search for, if null, fallback string will be used
     * @param fallback fallback string, returned in case no description was found
     * @return the description if available, fallback otherwise
     */
    default String getOrDefaultDescription(@Nullable String key, @Nullable String fallback) {
        return getOrDefaultDescription(key, fallback, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the description for a specific plugin description key, or fallback to a default string
     *
     * @param namespace the plugin namespace to search
     * @param key       description key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as description text and filled with arguments if no text is found
     * @return the description if available, fallback otherwise
     */
    default String getOrDefaultDescription(@Nullable PluginInfo namespace, @Nullable String key, @Nullable String fallback) {
        return getOrDefaultDescription(namespace, key, fallback, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Get the description for a specific bot description key, or fallback to a default string
     *
     * @param key       description key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as description text and filled with arguments if no text is found
     * @param arguments the arguments to put in the description (or in fallback if no description is found)
     * @return the description if available, fallback otherwise
     */
    default String getOrDefaultDescription(@Nullable String key, @Nullable String fallback, @NotNull Object... arguments) {
        return getOrDefaultDescription(null, key, fallback, arguments);
    }

    /**
     * Get the description for a specific plugin description key, or fallback to a default string
     *
     * @param namespace the plugin namespace to search
     * @param key       description key to search for, if null, fallback string will be used
     * @param fallback  fallback string, used as description text and filled with arguments if no text is found
     * @param arguments the arguments to put in the description (or in fallback if no description is found)
     * @return the description if available, fallback otherwise
     */
    String getOrDefaultDescription(@Nullable PluginInfo namespace, @Nullable String key, @Nullable String fallback, @NotNull Object... arguments);
}
