package it.sevenbits.backend.taskmanager.web.service.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateUserRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateUserResponse;

import java.util.List;

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

    /**
     * Get user by his name
     *
     * @param name name of the user
     * @return user from database
     */
    User getUserByName(String name);

    /**
     * Get all users in database
     *
     * @return list of users
     */
    List<User> getAllUsers();

    /**
     * Update user account
     *
     * @param id      user ID
     * @param request request with new account data
     * @return updated user
     */
    UpdateUserResponse updateUser(String id, UpdateUserRequest request);
}
