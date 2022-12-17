package eu.darkbot.api.extensions;

/**
 * Represents a module that is only set for a short-lived task
 * <br>
 * They are not available for users to select, they're installed by other modules or behaviors,
 * and they will clean up after themselves. If the user pauses and resumes the bot the temporal
 * modules also get reset to the proper module.
 * <br>
 * Extending {@code TemporalModule} in shared modules is recommended over implementing the interface yourself.
 */
public interface TemporalModule {

    /**
     * Go back to the previous module
     */
    void goBack();

    /**
     * The module to go back to, which must not be another temporal module,
     * otherwise infinite recursion may occur.
     * @return the previously used non-temporal module.
     */
    Module getBack();

}
