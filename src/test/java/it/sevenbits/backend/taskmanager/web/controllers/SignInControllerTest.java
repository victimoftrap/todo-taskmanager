package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignInResponse;
import it.sevenbits.backend.taskmanager.web.service.signin.SignInService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SignInControllerTest {
    private SignInController signInController;
    private SignInService mockSignInService;
    private JwtTokenService mockJwtTokenService;
    private User mockUser;

    @Before
    public void setup() {
        mockUser = mock(User.class);
        mockSignInService = mock(SignInService.class);
        mockJwtTokenService = mock(JwtTokenService.class);
        signInController = new SignInController(mockSignInService, mockJwtTokenService);
    }

    @Test
    public void testOkForSignIn() {
        String token = "token";
        SignInRequest request = new SignInRequest("user", "pass");

        when(mockSignInService.signIn(any(SignInRequest.class))).thenReturn(mockUser);
        when(mockJwtTokenService.createToken(any(User.class))).thenReturn(token);

        ResponseEntity<SignInResponse> answer = signInController.createSession(request);
        verify(mockSignInService, times(1))
                .signIn(request);
        verify(mockJwtTokenService, times(1))
                .createToken(mockUser);


        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNotNull(answer.getBody());
        assertEquals(token, answer.getBody().getToken());
    }

    @Test
    public void testNoSuchUserToSignIn() {
        SignInRequest request = new SignInRequest("user", "pass");

        when(mockSignInService.signIn(any(SignInRequest.class))).thenReturn(null);

        ResponseEntity<SignInResponse> answer = signInController.createSession(request);
        verify(mockSignInService, times(1))
                .signIn(request);

        assertEquals(HttpStatus.FORBIDDEN, answer.getStatusCode());
        assertNull(answer.getBody());
    }
}