package eu.darkbot.api.config.annotations;

import eu.darkbot.api.config.ConfigSetting;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Marks this field as a table
 * <p>
 * The field type MUST be a {@code Map<String, YourType>} where YourType is the
 * object that the table configures.
 * The first column will be the string, and subsequent columns will be defined by
 * the fields in your type.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * Extra controls to show on top of the table, by default a
     * search field, add, and remove buttons.
     * <p>
     * You may repeat controls multiple times, useful for custom controls
     * If you use {@link Control#CUSTOM} you must define {@link #customControls()} with the same amount of results.
     *
     * @return controls to show on top of the table
     */
    Control[] controls() default {Control.SEARCH, Control.ADD, Control.REMOVE};

    /**
     * Array of the {@link ControlBuilder} classes used to create the {@link Control#CUSTOM}
     * defined in {@link #controls()}.
     * The number of times {@link Control#CUSTOM} appears in {@link #controls()} must match
     * exactly the number of members in the resulting array.
     *
     * @return an array of custom control builder classes
     */
    Class<? extends ControlBuilder<?>>[] customControls() default {};

    /**
     * Provide your own custom model for the table, if none is provided a
     * generic table model will be used.
     *
     * @return class for the type of table model
     */
    Class<? extends TableModel> customModel() default TableModel.class;

    /**
     * Decorators that do final touch-ups to the table.
     * You may use this to register custom table editors, modify table or wrapper size, etc.
     * @return an array of decorators for the table
     */
    Class<? extends Decorator<?>>[] decorator() default {};


    enum Control {
        SEARCH, ADD, REMOVE, CUSTOM
    }

    interface ControlBuilder<T> {
        JComponent create(JTable table, ConfigSetting<Map<String, T>> setting);
    }

    interface Decorator<T> {
        void handle(JTable table,
                    JScrollPane scrollPane,
                    @Nullable JPanel wrapper,
                    ConfigSetting<Map<String, T>> setting);
    }
}
