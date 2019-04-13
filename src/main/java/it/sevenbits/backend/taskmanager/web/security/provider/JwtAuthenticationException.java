package it.sevenbits.backend.taskmanager.web.security.provider;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception of a authentication
 */
public class JwtAuthenticationException extends AuthenticationException {
    /**
     * Create exception
     *
     * @param msg message, describes exception
     * @param t   cause of exception
     */
    public JwtAuthenticationException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Create exception
     *
     * @param msg message, describes exception
     */
    public JwtAuthenticationException(final String msg) {
        super(msg);
    }
}
