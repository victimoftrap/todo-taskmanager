package it.sevenbits.backend.taskmanager.web.service.users;

import it.sevenbits.backend.taskmanager.core.model.User;

/**
 * Interface for work with users in server
 */
public interface UsersService {
    /**
     * Get user by ID
     *
     * @param id ID of the user
     * @return user from database
     */
    User getUserById(String id);
}
