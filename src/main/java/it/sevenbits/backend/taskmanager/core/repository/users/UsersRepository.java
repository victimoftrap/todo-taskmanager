package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;

import java.util.List;

/**
 * Interface for repository with users data
 */
public interface UsersRepository {
    /**
     * Create new account
     *
     * @param user user registration data
     * @return created user
     */
    User createUser(SignUpRequest user);

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

    /**
     * Update user account
     *
     * @param update updated fields of account
     * @return updated account model
     */
    User updateUser(User update);
}
