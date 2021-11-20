package eu.darkbot.impl;

import eu.darkbot.api.API;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.utils.Inject;
import eu.darkbot.impl.decorators.ClassDecorator;
import eu.darkbot.impl.decorators.PostInitializationDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class PluginApiImpl implements PluginAPI {

    protected final Set<Singleton> singletons = new HashSet<>();
    protected final Set<Singleton> weakSingletons = Collections.newSetFromMap(new WeakHashMap<>());
    protected final Set<Class<?>> implClasses = new HashSet<>();
    protected final Set<ClassDecorator<?>> decorators = new HashSet<>();

    protected final ThreadLocal<LinkedList<Class<?>>> creationStack = ThreadLocal.withInitial(LinkedList::new);

    public PluginApiImpl() {
        singletons.add(this);
        decorators.add(requireInstance(PostInitializationDecorator.class));
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

    /*
     * Impl note: this method is synchronized to prevent multiple threads from creating the same instances,
     * and messing up with the creation stack tracking circular dependencies.
     *
     * This should always be the called method over createNewInstance
     */
    private synchronized <T> T getOrCreate(Class<T> clazz) {
        Set<Singleton> workingSet = null;
        if (Singleton.class.isAssignableFrom(clazz)) {
            workingSet = clazz.getClassLoader() == getClass().getClassLoader() ? singletons : weakSingletons;
            for (Singleton implementation : workingSet) {
                if (clazz.isInstance(implementation))
                    return clazz.cast(implementation);
            }
        }
        return createNewInstance(clazz, workingSet);
    }

    /*
     * Impl note: should always use getOrCreate() instead, since it's synchronized
     */
    private <T> T createNewInstance(Class<T> clazz, @Nullable Set<Singleton> workingSet)
            throws UnsupportedOperationException {
        if (creationStack.get().contains(clazz)) {
            throw new UnsupportedOperationException("Circular dependency detected: " +
                    Stream.concat(creationStack.get().stream(), Stream.of(clazz))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(" -> ")));
        }

        creationStack.get().addLast(clazz);
        if (clazz.isInterface()) {
            try {
                return (T) createNewInstance(getImplementationClass(clazz), workingSet);
            } finally {
                creationStack.get().removeLast();
            }
        }

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
            if (workingSet != null && value instanceof Singleton)
                workingSet.add((Singleton) value);

            for (ClassDecorator<?> d : decorators)
                d.tryLoad(value);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Exception calling constructor for API: " + clazz.getName(), e);
        } finally {
            creationStack.get().removeLast();
        }

        return value;
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

    /*
     * Small helper method to get the best constructor for a class
     */
    private static Constructor<?> getBestConstructor(Class<?> clazz) {
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

    /*
     * Small helper method to get the implementation class for an interface
     */
    private Class<?> getImplementationClass(Class<?> itf) {
        return implClasses.stream()
                .filter(itf::isAssignableFrom)
                .findFirst()
                .orElseThrow(() ->
                        new UnsupportedOperationException("No implementation found for " + itf.getName()));
    }

}
