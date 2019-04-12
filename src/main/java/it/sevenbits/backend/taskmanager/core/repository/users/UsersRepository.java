package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;

import java.util.List;

/**
 * Interface for repository with users data
 */
public interface UsersRepository {
    /**
     * Find user by his ID
     *
     * @param id ID of the user
     * @return user
     */
    User findUserById(String id);

    /**
     * Find user by his name
     *
     * @param username name of the user
     * @return user
     */
    User findUserByName(String username);

    /**
     * Get all users
     *
     * @return list with users
     */
    List<User> findAll();
}
