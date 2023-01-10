package eu.darkbot.impl.config;

import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.util.ValueHandler;
import eu.darkbot.api.extensions.PluginInfo;
import eu.darkbot.util.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a setting that can be configured by the user
 */
public class ConfigSettingImpl<T> implements ConfigSetting<T> {

    private final @Nullable ConfigSetting.Parent<?> parent;
    private final String key;
    private final String name;
    private final String description;

    private final Class<T> type;
    private final ValueHandler<T> handler;
    private T value;

    private final Set<Consumer<T>> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    public ConfigSettingImpl(@Nullable ConfigSetting.Parent<?> parent,
                             String key, String name, String description,
                             Class<T> type,
                             ValueHandler<T> handler) {
        this.parent = parent;
        this.key = key;
        this.name = name;
        this.description = description;
        this.type = ReflectionUtils.wrapped(type);
        this.handler = handler;
    }

    public @Nullable ConfigSetting.Parent<?> getParent() {
        return parent;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Class<T> getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        value = handler.validate(value);
        if (this.value != value) {
            this.value = value;
            handler.updateParent(this);
        }
        listeners.forEach(l -> l.accept(this.value));
    }

    public void addListener(Consumer<T> listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        this.listeners.remove(listener);
    }

    public ValueHandler<T> getHandler() {
        return handler;
    }

    public <H> @Nullable H getHandler(Class<H> clazz) {
        if (clazz.isInstance(handler)) return clazz.cast(handler);
        return null;
    }

    public static abstract class Parent<T> extends ConfigSettingImpl<T> implements ConfigSetting.Parent<T> {
        private final Map<String, ConfigSetting<?>> children;

        public Parent(ConfigSetting.Parent<?> parent,
                      String key, String name, String description,
                      Class<T> type,
                      ValueHandler<T> handler,
                      Function<ConfigSetting.Parent<?>, Map<String, ConfigSetting<?>>> children) {
            super(parent, key, name, description, type, handler);
            this.children = children.apply(this);
        }

        public Map<String, ConfigSetting<?>> getChildren() {
            return children;
        }
    }

    public static class Root<T> extends Parent<T> {
        private final PluginInfo namespace;

        public Root(PluginInfo namespace,
                    String key, String name, String description,
                    Class<T> type,
                    ValueHandler<T> handler,
                    Function<ConfigSetting.Parent<?>, Map<String, ConfigSetting<?>>> children) {
            super(null, key, name, description, type, handler, children);
            this.namespace = namespace;
        }

        @Override
        public @Nullable PluginInfo getNamespace() {
            return namespace;
        }
    }

    public static class Intermediate<T> extends Parent<T> {
        public Intermediate(ConfigSetting.Parent<?> parent,
                            String key, String name, String description,
                            Class<T> type,
                            ValueHandler<T> handler,
                            Function<ConfigSetting.Parent<?>, Map<String, ConfigSetting<?>>> children) {
            super(parent, key, name, description, type, handler, children);
        }
    }

    public static class Leaf<T> extends ConfigSettingImpl<T> {

        public Leaf(ConfigSetting.Parent<?> parent, String key, String name, String description, Class<T> type,
                    ValueHandler<T> handler) {
            super(parent, key, name, description, type, handler);
        }

    }

}
