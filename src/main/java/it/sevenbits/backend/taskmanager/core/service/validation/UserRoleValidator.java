package it.sevenbits.backend.taskmanager.core.service.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for validating user roles
 */
public class UserRoleValidator implements Verifiable<String> {
    private List<String> userRoles;

    /**
     * Create validator
     */
    public UserRoleValidator() {
        userRoles = new ArrayList<>();
        Collections.addAll(userRoles, "ANONYMOUS", "USER", "ADMIN");
    }

    @Override
    public boolean verify(final String role) {
        return userRoles.contains(role);
    }
}
