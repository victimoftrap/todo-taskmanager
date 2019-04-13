package it.sevenbits.backend.taskmanager.web.service.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;

import java.util.List;

/**
 * Service that validates requests to part of database with users and generates responses
 */
public class WebUsersService implements UsersService {
    private final UsersRepository repository;
    private final Verifiable<String> idValidator;

    /**
     * Create service that validates requests and calls DAO
     *
     * @param repository  DAO, connected to database
     * @param idValidator validator of received ID
     */
    public WebUsersService(final UsersRepository repository, final Verifiable<String> idValidator) {
        this.repository = repository;
        this.idValidator = idValidator;
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
}
