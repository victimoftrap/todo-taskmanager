package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTaskRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private TaskRepository taskRepository;
    private RowMapper<Task> testMapper =
            (resultSet, i) ->
                    new Task(
                            eq(resultSet.getString(1)),
                            eq(resultSet.getString(2)),
                            eq(resultSet.getString(3)),
                            eq(resultSet.getString(4)),
                            eq(resultSet.getString(5))
                    );

    @Before
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        taskRepository = new DatabaseTaskRepository(mockJdbcOperations);
    }

//    @Test
//    public void testGetAllTasks() {
//        List<Task> mockTasks = mock(List.class);
//        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(mockTasks);
//
//        List<Task> answer = taskRepository.getTasks("inbox");
//        verify(mockJdbcOperations, times(1))
//                .query(
//                        eq("SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE status=?"),
//                        any(RowMapper.class),
//                        eq("inbox")
//                );
//        assertEquals(mockTasks, answer);
//    }

    @Test
    public void testGetTaskById() {
        String id = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenReturn(mockTask);

        Task answer = taskRepository.getTask(id);
        verify(mockJdbcOperations, times(1))
                .queryForObject(
                        eq("SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE id=?"),
                        any(RowMapper.class),
                        eq(id)
                );
        assertEquals(mockTask, answer);
    }

    @Test
    public void test() {
        String id = UUID.randomUUID().toString();
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenThrow(EmptyResultDataAccessException.class);

        Task answer = taskRepository.getTask(id);
        verify(mockJdbcOperations, times(1))
                .queryForObject(
                        eq("SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE id=?"),
                        any(RowMapper.class),
                        eq(id)
                );
        assertNull(answer);
    }
}