package it.sevenbits.backend.taskmanager.web.service.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateUserRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateUserResponse;

import java.util.List;

/**
 * Service that validates requests to part of database with users and generates responses
 */
public class UsersServiceImpl implements UsersService {
    private final UsersRepository repository;
    private final Verifiable<String> idValidator;
    private final Verifiable<String> userRoleValidator;

    /**
     * Create service that validates requests and calls DAO
     *
     * @param repository        DAO, connected to database
     * @param idValidator       validator of received ID
     * @param userRoleValidator validator of received user roles
     */
    public UsersServiceImpl(final UsersRepository repository,
                            final Verifiable<String> idValidator,
                            final Verifiable<String> userRoleValidator) {
        this.repository = repository;
        this.idValidator = idValidator;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public User getUserById(final String id) {
        if (id == null || !idValidator.verify(id)) {
            return null;
        }
        return repository.findUserById(id);
    }

    @Override
    public User getUserByName(final String name) {
        if (name == null) {
            return null;
        }
        return repository.findUserByName(name);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UpdateUserResponse updateUser(final String id, final UpdateUserRequest request) {
        User needed = getUserById(id);
        if (needed == null) {
            return new UpdateUserResponse("");
        }

        List<String> roles = request.getAuthorities();
        for (String role : roles) {
            if (!userRoleValidator.verify(role)) {
                return null;
            }
        }
        User updatedAccount = new User(id, needed.getUsername(), needed.getPassword(), request.isEnabled(), roles);
        repository.updateUser(updatedAccount);
        return new UpdateUserResponse(id);
    }
}
