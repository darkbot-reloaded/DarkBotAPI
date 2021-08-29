package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks this field as a percentage
 *
 * The field must be of type double, and the value will range from 0 to 100
 * You may fine tune the restriction by adding @Number forcing a more-defined min-max percentage
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Percentage {
}
