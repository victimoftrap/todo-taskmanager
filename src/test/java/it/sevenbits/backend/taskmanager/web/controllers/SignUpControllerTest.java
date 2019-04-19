package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignUpResponse;
import it.sevenbits.backend.taskmanager.web.service.signup.SignUpService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SignUpControllerTest {
    private SignUpController signUpController;
    private SignUpService mockSignUpService;

    @Before
    public void setup() {
        mockSignUpService = mock(SignUpService.class);
        signUpController = new SignUpController(mockSignUpService);
    }

    @Test
    public void testNoContentForSignUp() {
        SignUpRequest request = new SignUpRequest("user", "pass");
        SignUpResponse response = new SignUpResponse("id");
        when(mockSignUpService.createAccount(any(SignUpRequest.class))).thenReturn(response);

        ResponseEntity<Void> answer = signUpController.createAccount(request);
        verify(mockSignUpService, times(1))
                .createAccount(request);

        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    @Test
    public void testUsernameConflictForSignUp() {
        ResponseEntity<Void> answer = signUpController.createAccount(new SignUpRequest("user", "pass"));

        assertEquals(HttpStatus.CONFLICT, answer.getStatusCode());
        assertNull(answer.getBody());
    }
}