package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface I18nAPI extends API.Singleton {

    Object[] EMPTY = new Object[0];

    /**
     * Get the translation for a specific translation key
     * @param key translation key to search for
     * @return The translated text if available, a placeholder text otherwise
     */
    default String get(@NotNull String key) {
        return get(key, EMPTY);
    }

    /**
     * Get the translation for a specific translation key with arguments
     * @param key translation key to search for
     * @param arguments the arguments to put in the translated text
     * @return The translated text if available, a placeholder text otherwise
     */
    String get(@NotNull String key, @NotNull Object... arguments);

    /**
     * Get the translation for a specific translation key, or fallback to a default string
     * @param key translation key to search for, if null, fallback string will be used
     * @param fallback fallback string, returned in case no translation was found
     * @return the translated message if available, fallback otherwise
     */
    default String getOrDefault(@Nullable String key, @Nullable String fallback) {
        return getOrDefault(key, fallback, EMPTY);
    }

    /**
     * Get the translation for a specific translation key, or fallback to a default string
     * @param key translation key to search for, if null, fallback string will be used
     * @param fallback fallback string, used as translation text and filled with arguments if no text is found
     * @param arguments the arguments to put in the translated text (or in fallback if no translation is found)
     * @return the translated message if available, fallback otherwise
     */
    String getOrDefault(@Nullable String key, @Nullable String fallback, @NotNull Object... arguments);

}
