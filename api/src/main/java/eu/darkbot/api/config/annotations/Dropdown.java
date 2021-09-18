package eu.darkbot.api.config.annotations;

import eu.darkbot.api.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dropdown {
    /**
     * If the dropdown should allow multi selection
     * @return true for multi selection dropdowns, false for single selection
     */
    boolean multi() default false;

    /**
     * The class implementing the option list, a default implementation will cover
     * the case where enum or a collection of enum is the datatype, other cases
     * require this field to be set
     * @return the provider of options to use in the dropdown
     */
    Class<? extends Options<?>> options() default NullOptions.class;


    interface Options<T> extends API.Singleton {

        /**
         * List of the available options, will be called often
         * @return list with the available options to be picked
         */
        List<T> options();

        /**
         * Text to display for this setting, default to toString
         * @param option option to get text for
         * @return text to display
         */
        default String getText(T option) {
            return Objects.toString(option);
        }

        /**
         * Text to display as tooltip for this setting, default to null
         * @param option the option to get the tooltip for
         * @return null for no tooltip, the tooltip text otherwise
         */
        default String getTooltip(T option) {
            return null;
        }

    }

    interface NullOptions extends Options<Object> {}

}
