package eu.darkbot.api.config.types;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;

/**
 * Information saved about other players, most importantly the tags
 */
public interface PlayerInfo {

    /**
     * Last known username for this player, it could have changed later.
     * Will be updated periodically or when the user appears in sight.
     * @return the username for this player
     */
    String getUsername();

    /**
     * If the username is known, you may set it via this method.
     *
     * @param username the username to set for the user
     */
    void setUsername(String username);

    /**
     * @return the user id for this player
     */
    int getUserId();

    /**
     * @return the manager of the player tags for this user
     */
    PlayerTags getTags();

    /**
     * A handler for the tags assigned to a player
     */
    interface PlayerTags {

        /**
         * Add a tag to the player, indefinitely
         *
         * @param tag tag to set on the player
         * @return true if any update occurred, false if the tag has the same state as before
         */
        default boolean add(PlayerTag tag) {
            return add(tag, null);
        }

        /**
         * Add a temporal tag to the player
         *
         * @param tag tag to set on the player
         * @param expire instant at which this tag expires, null to never expire
         * @return true if any update occurred, false if the tag has the same state as before
         */
        boolean add(PlayerTag tag, @Nullable Instant expire);

        /**
         * Checks if the player has a specific tag
         *
         * @param tag the tag to check
         * @return true if the player currently has the tag, false otherwise
         */
        boolean contains(PlayerTag tag);

        /**
         * Removes a tag from the player
         *
         * @param tag the tag to remove
         * @return true if any update occurred, false if the player didn't have this tag already
         */
        boolean remove(PlayerTag tag);

        /**
         * @return a collection with all the tags for this player
         */
        Collection<? extends PlayerTag> get();

    }
}
