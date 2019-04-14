package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.web.service.signup.SignUpService;
import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignUpResponse;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Controller for handling requests to create new account
 */
@Controller
@RequestMapping(value = "/signup")
public class SignUpController {
    private final SignUpService signUpService;

    /**
     * Create controller
     *
     * @param signUpService service that creates new account
     */
    public SignUpController(final SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    /**
     * Create new account
     *
     * @param request request for creating new account
     * @return status of operation
     * 204 - account created
     * 409 - account with requested username has already exists
     */
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<Void> createAccount(@RequestBody @Valid final SignUpRequest request) {
        SignUpResponse response = signUpService.createAccount(request);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        return ResponseEntity
                .noContent()
                .build();
    }
}
