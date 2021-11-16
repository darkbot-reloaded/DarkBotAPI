package eu.darkbot.api.config.util;

import eu.darkbot.api.config.ConfigSetting;
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
     * @return a component that allows user to edit the setting
     */
    JComponent getEditorComponent(ConfigSetting<T> setting);

    /**
     * @return current value in the editor
     */
    T getEditorValue();

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return  true if editing was stopped; false otherwise
     *
     * @see CellEditor#stopCellEditing()
     */
    default boolean stopCellEditing() {
        return true;
    }

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     *
     * @see CellEditor#cancelCellEditing()
     */
    default void cancelCellEditing() {}

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
