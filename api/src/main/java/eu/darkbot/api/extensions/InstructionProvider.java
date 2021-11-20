package eu.darkbot.api.extensions;

import javax.swing.*;

/**
 * Allows your feature to provide instruction for the user in a few places.
 */
public interface InstructionProvider {

    /**
     * Basic instructions, just a string.
     *
     * If the feature is a module, this will display as a pop-up when the module is selected by the user.
     * Additionally, clicking the feature button on the plugins view will also display the message, which can
     * be used to view the message for non-module features.
     *
     * @return The string to show as instructions
     */
    default String instructions() {
        return null;
    }

    /**
     * Advanced instructions, show a custom component that will be shown inside an option pane.
     * Allows adding text, buttons, images, or whatever you please.
     *
     * The rules for when it shows are the exact same as {@link #instructions}.
     *
     * By defining this, the string instructions will be ignored.
     *
     * @return The JComponent to show as instructions.
     */
    default JComponent instructionsComponent() {
        return null;
    }


    /**
     * A component that will be shown at the top, inside the config window for this feature.
     *
     * You can use this to better explain to the user how to use the configuration of this feature,
     * or to set a button that can link them to other resources or views.
     *
     * @return The component to show before config tree in the feature config pop-up
     */
    default JComponent beforeConfig() {
        return null;
    }

}
