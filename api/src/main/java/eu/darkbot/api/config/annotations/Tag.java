package eu.darkbot.api.config.annotations;

import eu.darkbot.api.config.util.PlayerTag;

/**
 * Fine-tuning {@link PlayerTag} configuration fields
 */
public @interface Tag {
    /**
     * @return what to tell the user the default will be if left null
     */
    Default value() default Default.UNSET;

    enum Default {
        /**
         * Show a "unset" or equivalent if the tag is null
         */
        UNSET,
        /**
         * Show a "everyone" or equivalent if the tag is null
         */
        ALL,
        /**
         * Show a "no one" or equivalent if the tag is null
         */
        NONE
    }
}
