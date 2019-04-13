package it.sevenbits.backend.taskmanager.web.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Class for authenticated user in system
 */
public class AuthenticatedJwtToken extends AbstractAuthenticationToken {
    private final String subject;

    /**
     * Create authenticated user
     *
     * @param authorities permissions of a user, user roles in application
     * @param subject     name of the user
     */
    public AuthenticatedJwtToken(final Collection<? extends GrantedAuthority> authorities, final String subject) {
        super(authorities);
        this.subject = subject;
        setAuthenticated(true);
    }

    /**
     * Get data to authenticate
     *
     * @return data to authenticate
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Get data for identification user, that has this Authentication
     *
     * @return identification data
     */
    @Override
    public Object getPrincipal() {
        return subject;
    }
}
