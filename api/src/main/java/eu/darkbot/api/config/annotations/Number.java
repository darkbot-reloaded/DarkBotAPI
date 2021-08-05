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
}
