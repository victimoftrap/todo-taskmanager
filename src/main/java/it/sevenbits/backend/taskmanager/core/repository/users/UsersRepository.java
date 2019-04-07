package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;

import java.util.List;

/**
 * Interface for repository with users data
 */
public interface UsersRepository {
    /**
     * Find user by his name (login)
     *
     * @param login name of the user
     * @return user
     */
    User findUserByName(String login);

    /**
     * Get all users
     *
     * @return list with users
     */
    List<User> findAll();
}
