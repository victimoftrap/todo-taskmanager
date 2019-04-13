package it.sevenbits.backend.taskmanager.web.service.whoami;

import it.sevenbits.backend.taskmanager.core.model.User;

/**
 * Interface for recognizing current user
 */
public interface WhoAmIService {
    /**
     * Get information about current user
     *
     * @param token token, received in request header
     * @return user info
     */
    User getCurrentUserInfo(String token);
}
