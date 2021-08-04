package eu.darkbot.impl.decorators;

import eu.darkbot.api.events.Listener;
import eu.darkbot.api.managers.EventBrokerAPI;

public class ListenerDecorator extends AbstractDecorator<Listener> {

    private final EventBrokerAPI eventBroker;

    public ListenerDecorator(EventBrokerAPI eventBroker) {
        this.eventBroker = eventBroker;
    }

    @Override
    public void load(Listener listener) {
        eventBroker.registerListener(listener);
    }

    @Override
    public void unload(Listener listener) {
        eventBroker.unregisterListener(listener);
    }

}
