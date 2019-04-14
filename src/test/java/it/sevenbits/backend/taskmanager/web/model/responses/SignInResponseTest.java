package it.sevenbits.backend.taskmanager.web.model.responses;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignInResponseTest {
    @Test
    public void testModel() {
        String token = "tokennnn";
        SignInResponse response = new SignInResponse(token);

        assertNotNull(response);
        assertEquals(token, response.getToken());
    }
}