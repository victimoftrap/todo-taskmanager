package it.sevenbits.backend.taskmanager.web.model.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpRequestTest {
    @Test
    public void testModel() {
        String username = "user";
        String password = "password";
        SignUpRequest request = new SignUpRequest(username, password);

        assertNotNull(request);
        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }
}