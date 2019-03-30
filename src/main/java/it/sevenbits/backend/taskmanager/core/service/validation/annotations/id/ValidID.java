package it.sevenbits.backend.taskmanager.core.service.validation.annotations.id;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for validation ID
 */
@Documented
@Constraint(validatedBy = IDValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidID {
    /**
     * Message if validated param isn't valid
     * @return message
     */
    String message() default "ID not valid";

    /**
     * User to customize the targeted groups
     * @return groups
     */
    Class<?>[] groups() default {};

    /**
     * Extensibility purposes
     *
     * @return purposes
     */
    Class<? extends Payload>[] payload() default {};
}
