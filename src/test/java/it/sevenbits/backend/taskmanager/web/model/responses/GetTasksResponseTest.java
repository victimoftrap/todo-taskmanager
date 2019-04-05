package it.sevenbits.backend.taskmanager.web.model.responses;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.web.model.meta.GetTasksMetaData;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GetTasksResponseTest {
    @Test
    public void testResponse() {
        GetTasksMetaData mockMeta = mock(GetTasksMetaData.class);
        List<Task> mockTasks = mock(List.class);
        GetTasksResponse response = new GetTasksResponse(mockMeta, mockTasks);

        assertNotNull(response);
        assertEquals(mockMeta, response.getMeta());
        assertEquals(mockTasks, response.getTasks());
    }
}