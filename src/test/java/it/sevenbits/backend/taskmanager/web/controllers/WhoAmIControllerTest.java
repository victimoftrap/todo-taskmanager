package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WhoAmIControllerTest {
    private WhoAmIController whoAmIController;
    private WhoAmIService mockWhoAmIService;
    private User mockUser;

    @Before
    public void setup() {
        mockUser = mock(User.class);
        mockWhoAmIService = mock(WhoAmIService.class);
        whoAmIController = new WhoAmIController(mockWhoAmIService);
    }

    @Test
    public void testWhoAmI() {
        String token = "token";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);

        ResponseEntity<User> answer = whoAmIController.whoAmI(request);
        verify(mockWhoAmIService, times(1))
                .getCurrentUserInfo(token);

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockUser, answer.getBody());
    }
}