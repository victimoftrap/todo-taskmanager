package it.sevenbits.backend.taskmanager.core.service.validation;

import java.util.regex.Pattern;

/**
 * Class for ID validation
 */
public class IdValidator implements Verifiable<String> {
    private final Pattern pattern;

    /**
     * Create validator
     */
    public IdValidator() {
        pattern = Pattern.compile("^([a-fA-F0-9]{8}(-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12})$");
    }

    @Override
    public boolean verify(final String taskId) {
        return pattern.matcher(taskId).matches();
    }
}
