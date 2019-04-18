package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.config.settings.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.SortingOrderValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.StatusValidator;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskServiceImpl;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {
    private TaskRepository mockTaskRepository;
    private TaskService taskService;
    private MetaDataSettings mockSettings;

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);
        mockSettings = mock(MetaDataSettings.class);
        taskService = new TaskServiceImpl(
                mockTaskRepository,
                new IdValidator(),
                new StatusValidator(),
                new SortingOrderValidator(),
                mockSettings
        );
    }

    @Test
    public void testTaskCreated() {
        String owner = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        AddTaskRequest request = new AddTaskRequest(anyString());
        when(mockTaskRepository.createTask(request.getText(), anyString(), anyString())).thenReturn(mockTask);

        Task answer = taskService.createTask(owner, request);
        verify(mockTaskRepository, times(1)).createTask(anyString(), anyString(), anyString());
        assertEquals(mockTask, answer);
    }

    @Test
    public void testGetTasks() {
        String owner = UUID.randomUUID().toString();
        List<Task> mockList = mock(List.class);
        GetTasksRequest request = new GetTasksRequest("inbox", "asc", 1, 15);
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockList);

        List<Task> answer = taskService.getTasksByStatus(owner, request).getTasks();
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt());
        assertEquals(mockList, answer);
    }

    @Test
    public void testGetTaskById() {
        String owner = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.getTask(anyString(), anyString())).thenReturn(mockTask);

        Task answer = taskService.getTaskById(owner, owner);
        verify(mockTaskRepository, times(1)).getTask(anyString(), anyString());
        assertEquals(mockTask, answer);
    }

    @Test
    public void testRemoveTask() {
        String owner = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.removeTask(anyString(), anyString())).thenReturn(mockTask);

        Task answer = taskService.removeTaskById(owner, owner);
        verify(mockTaskRepository, times(1)).removeTask(anyString(), anyString());
        assertEquals(mockTask, answer);
    }

    @Test
    public void testUpdateTask() {
        String id = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        doAnswer(invocationOnMock -> {
            Task argument = invocationOnMock.getArgument(0);
            assertEquals(mockTask, argument);
            return null;
        }).when(mockTaskRepository).updateTask(id, mockTask);
    }
}