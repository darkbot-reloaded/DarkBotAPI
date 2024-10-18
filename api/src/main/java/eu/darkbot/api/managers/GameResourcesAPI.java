package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Provides access to in-game resources, like translation keys.
 * TODO: provide a way to search text-to-key
 */
public interface GameResourcesAPI extends API.Singleton {

    /**
     * @return used in-game {@link Locale}
     */
    @Nullable Locale getLanguage();

    /**
     * @param key of the translation
     * @return text in-game in the current language, or {@link Optional#empty()} if not found
     */
    Optional<String> findTranslation(@NotNull String key);

    /**
     * Retrieves a TranslationMatcher for the specified translation key with replacements.
     *
     * @param key          the translation key to search for
     * @param replacements optional strings to replace within the translation
     * @return an Optional containing the TranslationMatcher if the key is found, or Optional.empty() otherwise
     */
    @ApiStatus.AvailableSince("0.9.6")
    Optional<TranslationMatcher> getTranslationMatcher(@NotNull String key, String... replacements);

    interface TranslationMatcher {
        /**
         * Returns the locale used for the translation.
         *
         * @return the locale
         */
        Locale getLanguage();

        /**
         * Attempts to match the log with the translation.
         *
         * @param log the log string to search in
         * @return true if the translation key was found, false otherwise
         */
        boolean find(String log);

        /**
         * Returns the extracted group of text at the specified index.
         *
         * @param group the index of the group to retrieve starting from 1 - (0 is a whole text)
         * @return the extracted group of text, or null if not found
         */
        @Nullable String get(int group);

        /**
         * Returns the underlying matcher used to search for the translation key.
         *
         * @return the underlying matcher
         */
        Matcher getMatcher();
    }
}
