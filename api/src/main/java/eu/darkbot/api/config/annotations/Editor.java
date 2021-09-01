package eu.darkbot.api.config.annotations;

import eu.darkbot.api.config.util.OptionEditor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Editor {
    @SuppressWarnings("rawtypes")
    Class<? extends OptionEditor> value();
}
