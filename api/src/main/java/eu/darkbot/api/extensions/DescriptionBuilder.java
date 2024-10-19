package eu.darkbot.api.extensions;

import eu.darkbot.api.managers.I18nAPI;
import org.jetbrains.annotations.Nullable;

public interface DescriptionBuilder {

    /**
     * You can freely create descriptions for your tooltips or not by returning {@code null}
     * Default implementation will return {@code "Missing " + key} string if no translation is found
     *
     * @param namespace your plugin namespace or null if it bot namespace
     * @param key       base translation key
     * @param arguments passed arguments to the description
     * @return description text which will be used in tooltips or null to do not create description tooltip
     */
    @Nullable String getDescription(PluginInfo namespace, String key, Object... arguments);

    /**
     * Retrieves the description for a given translation key, falling back to a default string if not found.
     * <p>
     * This method allows for creating descriptions for tooltips with a fallback option.
     *
     * @param namespace your plugin namespace or {@code null} if using the bot namespace
     * @param key       the base translation key to look up the description
     * @param fallback  the fallback description to return if the key is not found
     * @param arguments the arguments to be used in the description or fallback (if applicable)
     * @return the description text to be used in tooltips if available, if not found, the fallback string will be returned
     */
    @Nullable String getOrDefaultDescription(PluginInfo namespace, String key, String fallback, Object... arguments);

    class Default implements DescriptionBuilder {
        private final I18nAPI i18n;

        public Default(I18nAPI i18n) {
            this.i18n = i18n;
        }

        @Override
        public @Nullable String getDescription(PluginInfo namespace, String key, Object... arguments) {
            return i18n.get(namespace, key + ".desc", arguments);
        }

        @Override
        public @Nullable String getOrDefaultDescription(PluginInfo namespace, String key, String fallback, Object... arguments) {
            return i18n.getOrDefault(namespace, key + ".desc", fallback, arguments);
        }
    }
}
