package it.sevenbits.backend.taskmanager.web.model.responses;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UpdateUserResponseTest {
    @Test
    public void testResponse() {
        String id = UUID.randomUUID().toString();
        UpdateUserResponse response = new UpdateUserResponse(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }
}