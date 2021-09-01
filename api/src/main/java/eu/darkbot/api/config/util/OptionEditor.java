package eu.darkbot.api.config.util;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public interface OptionEditor<T> {

    /**
     * Sets the current value for the editor.
     * This must discard anything related to the previous value, if any,
     * and set up the editor for the new setting.
     *
     * @param setting the setting to be edited
     * @param handler the value handler for the setting
     */
    JComponent getEditorComponent(T setting, ValueHandler<T> handler);

    /**
     * @return current value in the editor
     */
    T getEditorValue();

    /**
     * If the editor changes size during edition, it is recommended
     * to override the method to specify the bounds it will grow to,
     * so that the space is reserved.
     * Otherwise, the editor may be cut and not show the new content
     * when it becomes larger.
     *
     * @return the maximum size the component will occupy
     */
    default @Nullable Dimension getReservedSize() {
        return null;
    }

}
