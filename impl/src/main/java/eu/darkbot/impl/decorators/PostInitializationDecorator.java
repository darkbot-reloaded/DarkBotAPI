package eu.darkbot.impl.decorators;

import eu.darkbot.api.API;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.exceptions.FailedCreationException;
import eu.darkbot.api.utils.Inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostInitializationDecorator extends AbstractDecorator<Object> {

    private final PluginAPI api;

    public PostInitializationDecorator(PluginAPI api) {
        this.api = api;
    }

    @Override
    public void load(Object object) {
        Class<?> cl = object.getClass();
        do {
            for (Method method : cl.getDeclaredMethods()) {
                handleMethod(object, method);
            }
        } while ((cl = cl.getSuperclass()) != null && cl != Object.class);
    }

    private void handleMethod(Object object, Method method) {
        if (!method.isAnnotationPresent(Inject.class)) return;
        try {
            Object[] params = new Object[method.getParameterCount()];
            Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                Class<?> type = types[i];
                if (type.isInterface()) {
                    if (!API.class.isAssignableFrom(type))
                        throw new UnsupportedOperationException(object.getClass() + " -> " + method +
                                " requires " + type + " which is an interface without implementation.");
                    //noinspection unchecked
                    params[i] = api.requireAPI((Class<? extends API>) type);
                } else {
                    params[i] = api.requireInstance(type);
                }
            }

            method.invoke(object, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FailedCreationException(object.getClass() + " -> " + method +
                    " which requests @Inject had an exception calling", e);
        }
    }

    @Override
    public void unload(Object object) {}

}
