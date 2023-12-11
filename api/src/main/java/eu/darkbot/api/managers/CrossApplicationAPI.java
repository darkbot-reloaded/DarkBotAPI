package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.events.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * Special event handler that describes interface for sending/receiving serialized data between bot applications.
 * These events don't depend on game events, and using as a communication channel.
 * Every package contains UUID and raw string. UUID is a marker for determination message type to continue parsing message content.
 * For sending event: a plugin should call method {@link CrossApplicationAPI#sendEvent}.
 * For receiving outside events: a plugin should listen event using {@link OnMessage}.
 * {@link OnMessage} event implement {@link Comparable}.It should be used for comparing known {@link UUID}s.
 */
public interface CrossApplicationAPI extends API.Singleton {
    /**
     * Entry point for sending cross-application events
     * @param uuid unique identifier for exterminating message syntax
     * @param message raw message
     */
    void sendEvent(UUID uuid, String message);

    /**
     * @return true when existing active connection to router
     */
    boolean isActive();

    /**
     * Cross-application event listener massages that were received from router
     */
    class OnMessage implements Event, Comparable<UUID> {
        private final String message;

        /**
         * Creating event instance
         */
        public OnMessage(String message) {
            this.message = message;
        }

        /**
         * @return received message
         */
        public String getMessage() {
            return message.substring(36);
        }

        /**
         * Implementation of UUID comparator
         */
        @Override
        public int compareTo(@NotNull UUID uuid) {
            try {
                return uuid.compareTo(UUID.fromString(message.substring(0, 36)));
            } catch (Throwable i) {
                return -1;
            }
        }
    }
}
