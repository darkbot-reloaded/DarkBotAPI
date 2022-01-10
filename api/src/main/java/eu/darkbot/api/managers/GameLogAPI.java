package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;

/**
 * Provides access to read in-game log messages, you may read them
 * via the {@link LogMessageEvent} with a {@link eu.darkbot.api.events.Listener}.
 *
 * @see EventBrokerAPI
 */
public interface GameLogAPI extends API.Singleton {

    class LogMessageEvent implements Event {
        private final String message;

        public LogMessageEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
