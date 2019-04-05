package it.sevenbits.backend.taskmanager.web.model.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateTaskRequestTest {
    @Test
    public void testRequest() {
        String text = "None";
        String status = "inbox";
        UpdateTaskRequest request = new UpdateTaskRequest(text, status);

        assertNotNull(request);
        assertEquals(text, request.getText());
        assertEquals(status, request.getStatus());
    }
}