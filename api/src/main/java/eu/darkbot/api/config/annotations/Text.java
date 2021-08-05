package eu.darkbot.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fine-tuning {@link String} configuration fields
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Text {
    /**
     * How long of a text should the field accommodate for?
     * @return how many (roughly) 'm' character sized columns to accommodate
     */
    int length() default 10;

    /**
     * @return a placeholder text to show if the input is empty
     */
    String placeholder() default "";
}
