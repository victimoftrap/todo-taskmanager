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
     * Find user by his ID
     *
     * @param id          ID of the user
     * @param onlyEnabled if true than we would search in only enabled users
     * @return users
     */
    User findUserById(String id, boolean onlyEnabled);

    /**
     * Find user by his name
     *
     * @param username name of the user
     * @return user
     */
    User findUserByName(String username);

    /**
     * Find user by his name
     *
     * @param username    name of the user
     * @param onlyEnabled if true than we would search in only enabled users
     * @return user
     */
    User findUserByName(String username, boolean onlyEnabled);

    /**
     * Get all users
     *
     * @return list with users
     */
    List<User> findAll();

    /**
     * Get all users
     *
     * @param onlyEnabled if true than we would search in only enabled users
     * @return list with users
     */
    List<User> findAll(boolean onlyEnabled);

    /**
     * Update user account
     *
     * @param update updated fields of account
     * @return updated account model
     */
    User updateUser(User update);
}
