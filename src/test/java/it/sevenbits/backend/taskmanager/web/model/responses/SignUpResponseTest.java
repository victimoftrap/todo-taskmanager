package it.sevenbits.backend.taskmanager.web.model.responses;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SignUpResponseTest {
    @Test
    public void testModel() {
        String id = UUID.randomUUID().toString();
        SignUpResponse response = new SignUpResponse(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }
}