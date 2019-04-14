package it.sevenbits.backend.taskmanager.web.model.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignInRequestTest {
    @Test
    public void testRequest() {
        String username = "user";
        String password = "password";
        SignInRequest request = new SignInRequest(username, password);

        assertNotNull(request);
        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }
}