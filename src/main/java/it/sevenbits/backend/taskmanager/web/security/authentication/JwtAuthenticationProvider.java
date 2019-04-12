package it.sevenbits.backend.taskmanager.web.security.authentication;

import it.sevenbits.backend.taskmanager.web.security.authentication.model.NonAuthenticatedJwtToken;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom authentication provider
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JwtTokenService tokenService;

    /**
     * Create authentication token provider
     *
     * @param tokenService service that works with tokens
     */
    public JwtAuthenticationProvider(final JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Check can this provider work with received authentication type
     *
     * @param authentication auth type
     * @return true if supprted type
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return (NonAuthenticatedJwtToken.class.isAssignableFrom(authentication));
    }

    /**
     * Parse encoded token to Authentication
     *
     * @param authentication non-authenticated token
     * @return authenticated token
     * @throws AuthenticationException if cannot parse token
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        logger.debug("Authenticating {}", token);

        try {
            return tokenService.parseToken(token);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid token received", e);
        }
    }
}
