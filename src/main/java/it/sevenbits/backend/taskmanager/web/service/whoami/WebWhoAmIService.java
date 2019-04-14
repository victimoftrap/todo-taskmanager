package it.sevenbits.backend.taskmanager.web.service.whoami;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;

/**
 * Service that search for information about current user
 */
public class WebWhoAmIService implements WhoAmIService {
    private final UsersRepository usersRepository;
    private final JwtTokenService jwtTokenService;

    /**
     * Create service
     *
     * @param usersRepository repository with users
     * @param jwtTokenService service that works with user tokens
     */
    public WebWhoAmIService(final UsersRepository usersRepository, final JwtTokenService jwtTokenService) {
        this.usersRepository = usersRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public User getCurrentUserInfo(final String token) {
        if (token == null) {
            return null;
        }
        String username = jwtTokenService.parseToken(token).getPrincipal().toString();
        return usersRepository.findUserByName(username);
    }
}
