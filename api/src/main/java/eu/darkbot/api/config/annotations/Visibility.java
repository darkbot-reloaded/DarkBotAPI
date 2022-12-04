package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify a specific visibility value, this allows hiding complex
 * configurations from the default view that most users will see.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Visibility {
    Level value();

    enum Level {
        BASIC,
        INTERMEDIATE,
        ADVANCED,
        DEVELOPER;
    }
}
