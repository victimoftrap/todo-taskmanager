package it.sevenbits.backend.taskmanager.core.repository.tasks;

import it.sevenbits.backend.taskmanager.core.model.Task;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTaskRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private TaskRepository taskRepository;
    private Task mockTask;
    private String id;

    @Before
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        taskRepository = new DatabaseTaskRepository(mockJdbcOperations);
        mockTask = mock(Task.class);
        id = UUID.randomUUID().toString();
    }

    @Test
    public void testGetAllTasks() {
        String owner = UUID.randomUUID().toString();
        String status = "inbox";
        String order = "desc";
        int page = 1;
        int size = 20;
        int offset = (page - 1) * size;
        List<Task> mockTasks = mock(List.class);
        when(mockJdbcOperations.query(
                anyString(), any(RowMapper.class),
                anyString(), anyString(), anyInt(), anyInt())
        ).thenReturn(mockTasks);

        List<Task> answer = taskRepository.getTasks(owner, status, order, page, size);
        assertNotNull(answer);
    }

    @Test
    public void testGetTaskById() {
        Task mockTask = mock(Task.class);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString()))
                .thenReturn(mockTask);

        Task answer = taskRepository.getTask(id, id);
        verify(mockJdbcOperations, times(1))
                .queryForObject(
                        eq("SELECT id, text, status, createdAt, updatedAt, owner FROM tasks WHERE owner=? AND id=?"),
                        any(RowMapper.class),
                        eq(id),
                        eq(id)
                );
        assertEquals(mockTask, answer);
    }

    @Test
    public void notFoundTaskById() {
        String id = UUID.randomUUID().toString();
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString()))
                .thenThrow(EmptyResultDataAccessException.class);

        Task answer = taskRepository.getTask(id, id);
        verify(mockJdbcOperations, times(1))
                .queryForObject(
                        eq("SELECT id, text, status, createdAt, updatedAt, owner FROM tasks WHERE owner=? AND id=?"),
                        any(RowMapper.class),
                        eq(id),
                        eq(id)
                );
        assertNull(answer);
    }

    @Test
    public void testCreateTask() {
        String owner = UUID.randomUUID().toString();
        String text = "text";
        String status = "inbox";

        when(mockJdbcOperations.update(anyString(), anyString(), anyString(),
                anyString(), any(Timestamp.class), any(Timestamp.class), anyString())).thenReturn(1);

        Task task = taskRepository.createTask(text, status, owner);

        assertNotNull(task);
        assertEquals(text, task.getText());
        assertEquals(status, task.getStatus());
        verify(mockJdbcOperations, times(1)).update(
                eq("INSERT INTO tasks VALUES(?,?,?,?,?,?)"),
                eq(task.getId()),
                eq(text),
                eq(status),
                eq(Timestamp.valueOf(task.getCreatedAt())),
                eq(Timestamp.valueOf(task.getUpdatedAt())),
                eq(owner)
        );
    }

    @Test
    public void testRemoveTaskById() {
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString()))
                .thenReturn(mockTask);
        when(mockJdbcOperations.update(anyString(), anyString())).thenReturn(1);

        Task answer = taskRepository.removeTask(id, id);
        assertNotNull(answer);
        verify(mockJdbcOperations, times(1)).update(
                eq("DELETE FROM tasks WHERE owner=? AND id=?"),
                eq(id), eq(id)
        );
    }

    @Test
    public void testUpdateTaskById() {
        String text = "text";
        String status = "inbox";

        when(mockTask.getText()).thenReturn(text);
        when(mockTask.getStatus()).thenReturn(status);
        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), any(Timestamp.class), anyString()))
                .thenReturn(1);

        taskRepository.updateTask(id, mockTask);
        verify(mockJdbcOperations, times(1)).update(
                eq("UPDATE tasks SET text=?, status=?, updatedAt=? WHERE id=?"),
                eq(text),
                eq(status),
                any(Timestamp.class),
                eq(id)
        );
    }
}