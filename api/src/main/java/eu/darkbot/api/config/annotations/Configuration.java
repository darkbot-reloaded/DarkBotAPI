package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used as the header in configuration classes
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    /**
     * @return base translation key for the configuration root
     */
    String value();

    /**
     * If all fields are options by default.
     *
     * Fields can be marked with {@link Option} to opt in to being configurable options
     * Fields can be marked with {@link Option.Ignore} to opt out and be ignored
     *
     * @return true if by default all fields should be options, false otherwise
     */
    boolean allOptions() default true;
}
