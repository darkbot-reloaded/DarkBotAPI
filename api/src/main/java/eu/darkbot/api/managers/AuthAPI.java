package eu.darkbot.api.managers;

import org.jetbrains.annotations.Nullable;

public interface AuthAPI {
    /**
     * Sets up initial auth. Some environments (like servers) may have
     * less user friction by not requiring discord authentication.
     */
    void setupAuth();

    /**
     * Returns if the user has been validly authenticated with discord
     *
     * @return true if auth was performed & valid, false otherwise.
     */
    boolean isAuthenticated();

    /**
     * If the user didn't authenticate beforehand, it will prompt the user to authenticate.
     *
     * @return true if the user is a donor in the official darkbot discord server, false otherwise.
     */
    boolean isDonor();

    /**
     * If the user didn't authenticate beforehand, it will prompt the user to authenticate.
     * Will prompt the user to join the discord & donate if he hasn't done so yet.
     *
     * @return true if the user is a donor in the official darkbot discord server, false otherwise.
     */
    boolean requireDonor();

    /**
     * Returns a unique id for the player, the only guarantee is the same player will keep the same id
     * as long as the auth method is the same, and that no 2 players will share it.
     *
     * @return A unique string representing this user, null if no auth was done.
     */
    @Nullable
    String getAuthId();
}
