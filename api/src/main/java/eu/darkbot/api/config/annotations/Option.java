package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines this field as an option inside a config
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
    /**
     * The translation key this option should be bound to
     * Key is automatically inferred from the name of the field and parent fields in the
     * configuration tree, but you may set one to override that behavior
     *
     * @return The translation key to use for this setting
     */
    String value() default "";

    /**
     * Fields marked with ignore will not be part of the visible configuration for the user
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ignore {}
}
