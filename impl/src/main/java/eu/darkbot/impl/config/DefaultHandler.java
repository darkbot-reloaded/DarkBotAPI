package eu.darkbot.impl.config;

import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.annotations.Editor;
import eu.darkbot.api.config.annotations.Readonly;
import eu.darkbot.api.config.annotations.Visibility;
import eu.darkbot.api.config.util.ValueHandler;
import eu.darkbot.util.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultHandler<T> implements ValueHandler<T> {

    protected final Map<String, Object> metadata = new HashMap<>();
    protected final @Nullable Field field;

    public DefaultHandler() {
        this(null);
    }

    public DefaultHandler(@Nullable Field field) {
        this.field = field;
        if (field != null) {
            this.metadata.put("field", field);
            Editor editor = field.getAnnotation(Editor.class);
            if (editor != null)
                this.metadata.put("editor", editor.value());
            if (field.isAnnotationPresent(Readonly.class))
                this.metadata.put("readonly", true);

            Visibility visibility = field.getAnnotation(Visibility.class);
            if (visibility != null)
                this.metadata.put("visibility", visibility.value());
        }
    }

    @Override
    public T validate(T t) {
        return t;
    }

    @Override
    public void updateParent(ConfigSetting<T> setting) {
        ConfigSetting.Parent<?> parent = setting.getParent();
        if (parent != null && field != null) {
            Object parentObj = parent.getValue();
            if (parentObj != null) ReflectionUtils.set(field, parentObj, setting.getValue());
        }

        if (setting instanceof ConfigSettingImpl.Parent) {
            ConfigSettingImpl.Parent<T> current = (ConfigSettingImpl.Parent<T>) setting;
            current.getChildren().values().forEach(this::updateChild);
        }
    }

    @Override
    public void updateChildren(ConfigSetting<T> setting) {
        if (field == null) return;

        ConfigSetting.Parent<?> parent = setting.getParent();
        if (parent == null)
            throw new IllegalStateException("Cannot call to update children on a node without a parent!");

        Object parentObj = parent.getValue();
        setting.setValue(parentObj == null ? null :
                ReflectionUtils.get(field, parentObj, setting.getType()));
    }

    private <C> void updateChild(ConfigSetting<C> child) {
        child.getHandler().updateChildren(child);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> @Nullable V getMetadata(String key) {
        return (V) metadata.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> @Nullable V getOrCreateMetadata(String key, Supplier<V> builder) {
        return (V) metadata.computeIfAbsent(key, k -> builder.get());
    }

    @Override
    public String toString() {
        return "DefaultHandler{" +
                "metadata=" + metadata +
                ", field=" + field +
                '}';
    }

}
