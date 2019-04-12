package it.sevenbits.backend.taskmanager.web.security.authentication.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Class for non authenticated user in system (data, that we uses for authentication).
 */
public class NonAuthenticatedJwtToken extends AbstractAuthenticationToken {
    private final String token;

    /**
     * Create data of non-authenticated user
     *
     * @param token       non-authenticated token
     */
    public NonAuthenticatedJwtToken(final String token) {
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    /**
     * Change token authentication
     *
     * @param authenticated new auth
     */
    @Override
    public void setAuthenticated(final boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted");
        }
        super.setAuthenticated(false);
    }

    /**
     * Get data to authenticate
     *
     * @return data to authenticate
     */
    @Override
    public Object getCredentials() {
        return token;
    }

    /**
     * Get data for identification user, that has this Authentication
     *
     * @return identification data
     */
    @Override
    public Object getPrincipal() {
        return null;
    }
}
