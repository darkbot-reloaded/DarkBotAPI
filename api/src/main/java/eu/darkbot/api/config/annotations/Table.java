package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    Controls[] controls() default {Controls.SEARCH, Controls.ADD, Controls.REMOVE};

    enum Controls {
        SEARCH,
        ADD,
        REMOVE
    }
}
