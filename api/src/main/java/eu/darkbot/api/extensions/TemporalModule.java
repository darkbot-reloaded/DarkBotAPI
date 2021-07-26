package eu.darkbot.api.extensions;

/**
 * Represents a module that is only set for a short-lived task
 *
 * They are not available for users to select, they're installed by other modules or behaviors,
 * and they will clean-up after themselves. If the user pauses and resumes the bot the temporal
 * modules also get reset to the proper module.
 */
public interface TemporalModule {

    /**
     * Go back to the previous module
     */
    void goBack();

}
