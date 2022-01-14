package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fine-tuning number configurations (int, long, float or double)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Number {
    /**
     * @return minimum value this configuration should have
     */
    double min() default 0;
    /**
     * @return maximum value this configuration should have
     */
    double max() default 100;

    /**
     * @return how much should the value go up/down with a user click
     */
    double step() default 5;

    /**
     * Add a checkbox to be able to disable this field
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Disabled {
        /**
         * @return the value for the config if the checkbox is unchecked
         */
        double value() default -1;

        /**
         * @return the default value to show if the field is disabled
         */
        double def() default 0;
    }
}
