package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTaskRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private TaskRepository taskRepository;

    @Before
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        taskRepository = new DatabaseTaskRepository(mockJdbcOperations);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> mockTasks = mock(List.class);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(mockTasks);

        List<Task> answer = taskRepository.getTasks("inbox");
        verify(mockJdbcOperations, times(1))
                .query(
                        eq("SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE status=?"),
                        any(RowMapper.class),
                        eq("inbox"));
        assertEquals(mockTasks, answer);
    }
}