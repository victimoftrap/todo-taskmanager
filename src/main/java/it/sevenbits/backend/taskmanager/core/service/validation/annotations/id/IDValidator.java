package it.sevenbits.backend.taskmanager.core.service.validation.annotations.id;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Realisation of ID validator
 */
public class IDValidator implements ConstraintValidator<ValidID, String> {
    private Pattern pattern;

    @Override
    public void initialize(final ValidID constraintAnnotation) {
        pattern = Pattern.compile("^([a-fA-F0-9]{8}(-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12})$");
    }

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(s).matches();
    }
}
