package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fine-tuning {@link eu.darkbot.api.config.types.PlayerTag} configuration fields
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {
    /**
     * @return what to tell the user the default will be if left null
     */
    Default value() default Default.UNSET;

    enum Default {
        /**
         * Show an "unset" or equivalent if the tag is null
         */
        UNSET,
        /**
         * Show an "everyone" or equivalent if the tag is null
         */
        ALL,
        /**
         * Show a "no one" or equivalent if the tag is null
         */
        NONE
    }
}
