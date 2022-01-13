package eu.darkbot.impl.managers;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.events.EventHandler;
import eu.darkbot.api.events.Listener;
import eu.darkbot.api.extensions.PluginInfo;
import eu.darkbot.api.managers.ExtensionsAPI;
import eu.darkbot.api.managers.I18nAPI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class I18n implements I18nAPI, Listener {

    private final PluginAPI pluginAPI;
    private ExtensionsAPI extensions;

    private final PropertyContainer props = new PropertyContainer(null);
    private final Map<PluginInfo, PropertyContainer> pluginProps = new HashMap<>();
    private Locale locale;

    public I18n(PluginAPI pluginAPI) {
        // Due to I18n being such an integral part, we cannot afford a direct dependency here.
        // If we include extensions api here, it forces extensionsAPI not to rely on i18n on initialization.
        // The solution is for i18n to lazy-load the dependency when needed later on.
        this.pluginAPI = pluginAPI;
    }

    public void setLocale(Locale locale) {
        if (this.locale == locale) return;
        this.locale = locale;
        reloadResources();
    }

    public Locale getLocale() {
        return locale;
    }

    public void reloadResources() {
        props.loadResources(locale, "", getClass().getClassLoader());
        pluginProps.forEach((pi, pc) ->
                pc.loadResources(locale, pi.getBasePackage(), extensions.getClassLoader(pi)));
    }

    @EventHandler
    public void onPluginChange(ExtensionsAPI.PluginLifetimeEvent ev) {
        ExtensionsAPI.PluginStage stage = ev.getStage();
        switch (stage) {
            case BEFORE_LOAD:
                pluginProps.clear();
                break;
            case AFTER_LOAD:
                if (extensions == null)
                    this.extensions = pluginAPI.requireAPI(ExtensionsAPI.class);
                extensions.getPluginInfos().stream()
                        .filter(pi -> pi.getBasePackage() != null)
                        .forEach(pi -> pluginProps.put(pi, new PropertyContainer(props)));
                reloadResources();
        }
    }

    private PropertyContainer getProps(PluginInfo namespace) {
        return namespace == null ? props : pluginProps.getOrDefault(namespace, props);
    }


    private String getInternal(PluginInfo namespace, @NotNull String key) {
        if (key == null) throw new IllegalArgumentException("Translation key must not be null");
        String res = getProps(namespace).getProperty(key);
        return res != null ? res : "Missing " + key;
    }

    private String getOrDefaultInternal(PluginInfo namespace, String key, String fallback) {
        if (key == null) return fallback;
        String res = getProps(namespace).getProperty(key);
        return res != null ? res : fallback;
    }

    public String get(PluginInfo namespace, @NotNull String key, Object... arguments) {
        return MessageFormat.format(getInternal(namespace, key), arguments);
    }

    public String getOrDefault(PluginInfo namespace, String key, String fallback, Object... arguments) {
        String text = getOrDefaultInternal(namespace, key, fallback);
        if (text == null) return null;
        return MessageFormat.format(text, arguments);
    }


    private static class PropertyContainer {
        private final Properties fallback, props;

        public PropertyContainer(PropertyContainer parent) {
            this.fallback = new Properties(parent == null ? null : parent.props);
            this.props = new Properties(fallback);
        }

        public void loadResources(Locale locale, String basePackage, ClassLoader loader) {
            fallback.clear();
            props.clear();

            loadResource(fallback, getLangFile(Locale.ENGLISH, basePackage, loader));
            if (!locale.equals(Locale.ENGLISH)) loadResource(props, getLangFile(locale, basePackage, loader));
        }

        private URL getLangFile(Locale locale, String base, ClassLoader loader) {
            if (!base.isEmpty()) base = base.replace(".", "/") + "/";
            String filePath = base + "lang/strings_" + locale.toLanguageTag() + ".properties";
            URL res = loader.getResource(filePath);

            if (res == null) System.out.println("Couldn't find translation file for " + locale + " in " + filePath);
            return res;
        }

        private void loadResource(Properties props, URL resource) {
            if (resource == null) return;
            try {
                // We must explicitly disable cache, as otherwise plugins will fail to unload.
                URLConnection connection = resource.openConnection();
                connection.setUseCaches(false);

                try (InputStream is = connection.getInputStream();
                     InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    props.load(isr);
                }
            } catch (IOException e) {
                System.err.println("Failed to load translations: " + resource);
                e.printStackTrace();
            }
        }

        public String getProperty(String key) {
            return props.getProperty(key);
        }
    }


}
