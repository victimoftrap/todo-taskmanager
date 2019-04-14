package it.sevenbits.backend.taskmanager.web.service.signup;

import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignUpResponse;

/**
 * Interface for validating request for creating new account
 */
public interface SignUpService {
    /**
     * Create new account
     *
     * @param request request with user personal data
     * @return response with info about operation
     */
    SignUpResponse createAccount(SignUpRequest request);
}
