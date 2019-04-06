package it.sevenbits.backend.taskmanager.core.service.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for status validation
 */
public class StatusValidator implements Verifiable<String> {
    private List<String> statuses;

    /**
     * Create validator
     */
    public StatusValidator() {
        statuses = new ArrayList<>();
        Collections.addAll(statuses, "inbox", "done");
    }

    @Override
    public boolean verify(final String param) {
        if (param == null) {
            return false;
        }
        return statuses.contains(param.toLowerCase());
    }
}
