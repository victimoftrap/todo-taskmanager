package it.sevenbits.backend.taskmanager.web.service.whoami;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;

/**
 * Service that search for information about current user
 */
public class WebWhoAmIService implements WhoAmIService {
    private final UsersService usersService;
    private final JwtTokenService jwtTokenService;

    /**
     * Create service
     *
     * @param usersService    service that works with users in database
     * @param jwtTokenService service that works with user tokens
     */
    public WebWhoAmIService(final UsersService usersService, final JwtTokenService jwtTokenService) {
        this.usersService = usersService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public User getCurrentUserInfo(final String token) {
        if (token == null) {
            return null;
        }
        String username = jwtTokenService.parseToken(token).getPrincipal().toString();
        return usersService.getUserByName(username);
    }
}
