package eu.darkbot.api.config.types;

import eu.darkbot.api.game.entities.Box;

/**
 * Predefined settings for {@link Box} customized by user.
 */
public interface BoxInfo {

    /**
     * @return True if the user wants to collect these type of boxes, false otherwise
     */
    boolean shouldCollect();

    /**
     * @param shouldCollect if this box info should be collected
     */
    void setShouldCollect(boolean shouldCollect);

    /**
     * @return How long the user wants to wait for the box to be picked up in milliseconds
     */
    int getWaitTime();

    /**
     * @param waitTime Wait time to collect this resource in milliseconds
     */
    void setWaitTime(int waitTime);

    /**
     * @return How important this box is to the user, the lower number is more important
     */
    int getPriority();

    /**
     * @param priority the priority to assign this box info
     */
    void setPriority(int priority);
}
