package it.sevenbits.backend.taskmanager.core.repository.tasks;

import it.sevenbits.backend.taskmanager.core.model.Task;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MapTaskRepositoryTest {
    private TaskRepository taskRepository;
    private Map<String, Task> mockContainer;
    private Task mockTask;
    private String id;
    private String owner;

    @Before
    public void setup() {
        mockContainer = mock(Map.class);
        taskRepository = new MapTaskRepository(mockContainer);
        mockTask = mock(Task.class);
        id = UUID.randomUUID().toString();
        owner = UUID.randomUUID().toString();
    }

    @Test
    public void testCreateTask() {
        String text = "text";
        String status = "done";
        Task task = taskRepository.createTask(text, status, owner);

        assertNotNull(task);
        assertEquals(text, task.getText());
        assertEquals(status, task.getStatus());
        assertEquals(owner, task.getOwner());
    }

    @Test
    public void testGetAllTasks() {
        taskRepository = new MapTaskRepository(new HashMap<>());
        Task task1 = taskRepository.createTask("text1", "inbox", owner);
        Task task2 = taskRepository.createTask("text2", "done", owner);
        Task task3 = taskRepository.createTask("text3", "inbox", owner);
        Task task4 = taskRepository.createTask("text4", "inbox", UUID.randomUUID().toString());

        List<Task> answer = taskRepository.getTasks(owner, "inbox", "asc", 1, 20);
        assertTrue(Arrays.asList(task1, task3).containsAll(answer));
        assertEquals(
                Arrays.asList(task2),
                taskRepository.getTasks(owner, "done", "asc", 1, 20)
        );
    }

    @Test
    public void testGetTask() {
        when(mockContainer.get(anyString())).thenReturn(mockTask);

        Task answer = taskRepository.getTask(id, owner);
        verify(mockContainer, times(1)).get(id);
        assertNotNull(answer);
        assertEquals(mockTask, answer);
    }

    @Test
    public void testNotFoundTask() {
        when(mockContainer.get(anyString())).thenReturn(null);

        Task answer = taskRepository.getTask(id, owner);
        verify(mockContainer, times(1)).get(id);
        assertNull(answer);
    }

    @Test
    public void testRemoveTask() {
        when(mockContainer.remove(anyString())).thenReturn(mockTask);

        Task answer = taskRepository.removeTask(id, owner);
        verify(mockContainer, times(1)).remove(id);
        assertNotNull(answer);
    }

    @Test
    public void testUpdateTask() {
        doAnswer(invocationOnMock -> {
            assertEquals(id, invocationOnMock.getArgument(0));
            assertEquals(mockTask, invocationOnMock.getArgument(1));
            return null;
        }).when(mockContainer).replace(id, mockTask);


        taskRepository.updateTask(id, mockTask);
        verify(mockContainer, times(1))
                .replace(id, mockTask);
    }

    @Test
    public void testTaskCount() {
        taskRepository = new MapTaskRepository(new HashMap<>());
        taskRepository.createTask("text1", "inbox", owner);
        taskRepository.createTask("text2", "done", owner);
        taskRepository.createTask("text3", "inbox", owner);
        taskRepository.createTask("text4", "inbox", UUID.randomUUID().toString());

        assertEquals(2, taskRepository.getCountTasks(owner, "inbox"));
        assertEquals(1, taskRepository.getCountTasks(owner, "done"));
    }
}