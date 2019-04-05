package it.sevenbits.backend.taskmanager.web.model;

import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTaskRequestTest {
    @Test
    public void testRequest() {
        String text = "Some text";
        AddTaskRequest request = new AddTaskRequest(text);

        assertNotNull(request);
        assertEquals(text, request.getText());
    }
}