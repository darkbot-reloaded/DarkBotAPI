package eu.darkbot.api.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Draw {

    Stage value();

    Attach attach() default Attach.AFTER;

    enum Stage {
        ZONES,
        INFO_AND_HEALTH,
        HERO_TRAIL,
        CONSTANT_ENTITIES,
        DYNAMIC_ENTITIES,
        HERO_AND_PET,
        DEV_STUFF,
        OVERLAY
    }

    enum Attach {
        BEFORE,
        REPLACE,
        AFTER
    }
}
