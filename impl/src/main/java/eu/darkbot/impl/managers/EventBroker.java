package eu.darkbot.impl.managers;

import eu.darkbot.api.events.Event;
import eu.darkbot.api.events.EventHandler;
import eu.darkbot.api.events.Listener;
import eu.darkbot.api.managers.EventBrokerAPI;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class EventBroker implements EventBrokerAPI {

    private final WeakHashMap<Listener, EventDispatcher> dispatchers = new WeakHashMap<>();

    private final Set<Listener> toAdd = Collections.newSetFromMap(new WeakHashMap<>());
    private final Set<Listener> toRemove = Collections.newSetFromMap(new WeakHashMap<>());
    // Handle concurrent events being sent to listeners
    private int eventsBeingSent = 0;

    @Override
    public synchronized void sendEvent(@NotNull Event event) {
        try {
            eventsBeingSent++;
            dispatchers.forEach((l, d) -> d.handle(l, event));
        } finally {
            eventsBeingSent--;
        }
        if (eventsBeingSent == 0) {
            toAdd.forEach(this::registerListener);
            toRemove.forEach(dispatchers::remove);
        }
    }

    @Override
    public synchronized void registerListener(@NotNull Listener listener) {
        if (eventsBeingSent == 0) dispatchers.put(listener, new EventDispatcher(listener.getClass()));
        else toAdd.add(listener);
    }

    @Override
    public synchronized void unregisterListener(@NotNull Listener listener) {
        if (eventsBeingSent == 0) dispatchers.remove(listener);
        else toRemove.add(listener);
    }

    private static class EventDispatcher {
        private final List<EventMethod> methods;

        public EventDispatcher(Class<?> clazz) {
            this.methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(EventHandler.class))
                    .map(EventMethod::new)
                    .collect(Collectors.toList());
        }

        public void handle(Listener listener, Event event) {
            methods.forEach(m -> m.handle(listener, event));
        }

    }

    private static class EventMethod {
        private final Method method;
        private final Class<?> clazz;

        public EventMethod(Method method) {
            if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                throw new IllegalArgumentException("@EventHandler must have a single event parameter: " + method);
            this.method = method;
            this.clazz = method.getParameterTypes()[0];
        }

        public void handle(Listener listener, Event event) {
            if (!clazz.isInstance(event)) return;
            try {
                method.invoke(listener, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println("Exception passing " +
                        event.getClass().getName() + "to " +
                        listener.getClass().toString() + "#" + method.getName());
                e.printStackTrace();
            }
        }
    }


}
