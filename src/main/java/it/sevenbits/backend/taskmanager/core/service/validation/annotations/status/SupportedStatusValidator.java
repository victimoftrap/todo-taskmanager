package it.sevenbits.backend.taskmanager.core.service.validation.annotations.status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Realisation of task status validator
 */
public class SupportedStatusValidator implements ConstraintValidator<SupportedStatus, String> {
    private List<String> statuses;

    @Override
    public void initialize(final SupportedStatus constraintAnnotation) {
        statuses = new ArrayList<>();
        Collections.addAll(statuses, "inbox", "done");
    }

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return statuses.contains(s);
    }
}
