package it.sevenbits.backend.taskmanager.web.service.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;

/**
 * Service that validates requests to part of database with users and generates responses
 */
public class WebUsersService implements UsersService {
    private final UsersRepository repository;
    private final Verifiable<String> idValidator = new IdValidator();

    /**
     * Create service that validates requests and calls DAO
     *
     * @param repository DAO, connected to database
     */
    public WebUsersService(final UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUserById(final String id) {
        if (id == null || !idValidator.verify(id)) {
            return null;
        }
        // TODO check authorities (ADMIN required)
        return repository.findUserById(id);
    }
}
