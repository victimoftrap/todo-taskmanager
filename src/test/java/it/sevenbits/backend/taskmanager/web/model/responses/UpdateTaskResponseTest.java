package it.sevenbits.backend.taskmanager.web.model.responses;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UpdateTaskResponseTest {
    @Test
    public void testResponse() {
        String id = UUID.randomUUID().toString();
        UpdateTaskResponse response = new UpdateTaskResponse(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }
}