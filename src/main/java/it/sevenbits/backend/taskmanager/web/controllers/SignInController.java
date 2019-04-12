package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignInResponse;
import it.sevenbits.backend.taskmanager.web.service.signin.SignInService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for handling sign in requests
 */
@Controller
@RequestMapping("/signin")
public class SignInController {
    private final SignInService signInService;
    private final JwtTokenService tokenService;

    /**
     * Create controller for signing in user
     *
     * @param signInService service that verifying requests
     */
    public SignInController(final SignInService signInService,
                            @Qualifier(value = "jwtTokenService") final JwtTokenService tokenService) {
        this.signInService = signInService;
        this.tokenService = tokenService;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<SignInResponse> createSession(@RequestBody final SignInRequest request) {
        User user = signInService.signIn(request);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        String token = tokenService.createToken(user);
        return ResponseEntity
                .ok()
                .body(new SignInResponse(token));
    }
}
