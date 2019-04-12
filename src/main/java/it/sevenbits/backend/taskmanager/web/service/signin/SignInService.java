package it.sevenbits.backend.taskmanager.web.service.signin;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignInRequest;

/**
 * Interface for validating sign in user authentication
 */
public interface SignInService {
    /**
     * Sign in user
     *
     * @param request request with received username and password
     * @return user or null if password doesn't match
     */
    User signIn(SignInRequest request);
}
