package eu.darkbot.impl;

import eu.darkbot.api.API;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.utils.Inject;
import eu.darkbot.impl.decorators.ClassDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

@SuppressWarnings("unchecked")
public class PluginApiImpl implements PluginAPI {

    protected final Set<Singleton> singletons = new HashSet<>();
    protected final Set<Singleton> weakSingletons = Collections.newSetFromMap(new WeakHashMap<>());
    protected final Set<Class<?>> implClasses = new HashSet<>();
    protected final Set<ClassDecorator<?>> decorators = new HashSet<>();

    public PluginApiImpl() {
        singletons.add(this);
    }

    /**
     * Adds singleton instances that will be used to fill-in api parameters
     * @param singletons singletons to use
     */
    public void addInstance(Singleton... singletons) {
        Collections.addAll(this.singletons, singletons);
    }

    public void addDecorator(ClassDecorator<?>... decorators) {
        Collections.addAll(this.decorators, decorators);
    }

    /**
     * Adds what implementation classes should be used for different APIs
     * @param implementations implementation classes to use
     */
    @SafeVarargs
    public final void addImplementations(Class<? extends API>... implementations) {
        Collections.addAll(this.implClasses, implementations);
    }

    private <T extends Singleton> T getOrCreateSingleton(Class<T> clazz) throws UnsupportedOperationException {
        Set<Singleton> workingSet = clazz.getClassLoader() == getClass().getClassLoader() ? singletons : weakSingletons;
        for (Singleton implementation : workingSet) {
            if (clazz.isInstance(implementation))
                return clazz.cast(implementation);
        }
        T impl = createNewInstance(clazz);
        workingSet.add(impl);
        return impl;
    }

    private Constructor<?> getBestConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0)
            throw new UnsupportedOperationException("No public constructor exists for " + clazz.getName());
        // Ideal case, just one constructor to call
        if (constructors.length == 1) return constructors[0];

        // For multiple constructors, search for the @Inject annotation
        Constructor<?> result = null;
        for (Constructor<?> c : constructors) {
            if (c.getAnnotation(Inject.class) == null) continue;
            if (result != null)
                throw new UnsupportedOperationException("Found multiple @Inject constructors in " + clazz.getName() +
                        ". You must only annotate one constructor with @Inject");
            result = c;
        }
        if (result == null)
            throw new UnsupportedOperationException("Multiple constructors were found in " + clazz.getName() +
                    ". You must either annotate one with @Inject or");
        return result;
    }

    private <T> T createNewInstance(Class<T> clazz) throws UnsupportedOperationException {
        if (clazz.isInterface())
            return (T) createNewInstance(implClasses.stream()
                    .filter(clazz::isAssignableFrom)
                    .findFirst()
                    .orElseThrow(() ->
                            new UnsupportedOperationException("No implementation found for " + clazz.getName())));

        Constructor<?> constructor = getBestConstructor(clazz);

        T value;
        try {
            // Not doing this in a stream in an attempt to keep stack traces as clear as possible
            Object[] params = new Object[constructor.getParameterCount()];
            Class<?>[] types = constructor.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                params[i] = getOrCreate(types[i]);
            }

            value = (T) constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Exception calling constructor for API: " + clazz.getName(), e);
        }
        decorators.forEach(d -> d.tryLoad(value));
        return value;
    }

    private <T> T getOrCreate(Class<T> clazz) {
        if (Singleton.class.isAssignableFrom(clazz))
            return (T) getOrCreateSingleton((Class<Singleton>) clazz);
        return createNewInstance(clazz);
    }

    @Override
    public @Nullable <T extends API> T getAPI(@NotNull Class<T> api) {
        try {
            return requireAPI(api);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public @NotNull <T extends API> T requireAPI(@NotNull Class<T> api) throws UnsupportedOperationException {
        if (!api.isInterface())
            throw new UnsupportedOperationException("Can't get API from implementation " +
                    api.getName() + ", use the API interface");

        return getOrCreate(api);
    }

    @Override
    public @NotNull <T> T requireInstance(@NotNull Class<T> clazz) throws UnsupportedOperationException {
        if (clazz.isInterface())
            throw new UnsupportedOperationException("Can't create instance from interface " +
                    clazz.getName() + ", use requireAPI instead");

        return getOrCreate(clazz);
    }

}
