package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.extensions.Task;

/**
 * Allows different hangar-related actions to be performed.
 * Should only be called from background thread, used in {@link Task}s.
 *
 * @see Task
 * @since 0.7.1
 */
public interface HangarAPI extends API.Singleton {

    /**
     * Update the current hangar
     * Use {@link #requestUpdateCurrentHangar(long)} if you need it to update
     * continuously.
     */
    void updateCurrentHangar();

    /**
     * Force hangar list update.
     * Use {@link #requestUpdateHangarList(long)} if you need it to update
     * continuously.
     */
    void updateHangarList();

    /**
     * Returns the ID of the current hangar.
     * A value of 0 indicates that the currentHangar has not been initialized.
     */
    int getCurrentHangarId();

    /**
     * Request hangar list to be updated within a certain timeframe.
     * This method should be repeatedly called to request updates
     *
     * @param millis The maximum time to wait
     */
    void requestUpdateCurrentHangar(long millis);

    /**
     * Request current hangar to be updated within a certain timeframe.
     * This method should be repeatedly called to request updates
     *
     * @param millis The maximum time to wait
     */
    void requestUpdateHangarList(long millis);

    /**
     * Request to change the current hangar.
     * The ship must be in a base or disconnected.
     *
     * @param hangarId Id of the hangar to which the change is to be made
     * @return true if the request was successful
     */
    boolean changeHangar(int hangarId);
}
