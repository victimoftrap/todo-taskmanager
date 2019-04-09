package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TaskControllerServiceTest {
    private TaskRepository mockTaskRepository;
    private TaskService taskService;

//    @Before
//    public void setup() {
//        mockTaskRepository = mock(TaskRepository.class);
//        taskService = new TaskControllerService(mockTaskRepository);
//    }

    @Test
    public void testTaskCreated() {
        Task mockTask = mock(Task.class);
        AddTaskRequest request = new AddTaskRequest(anyString());
        when(mockTaskRepository.createTask(request.getText(), anyString())).thenReturn(mockTask);

        Task answer = taskService.createTask(request);
        verify(mockTaskRepository, times(1)).createTask(anyString(), anyString());
        assertEquals(mockTask, answer);
    }

    @Test
    public void testGetTasks() {
        List<Task> mockList = mock(List.class);
        GetTasksRequest request = new GetTasksRequest("inbox", "asc", 1, 15);
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockList);

        List<Task> answer = taskService.getTasksByStatus(request).getTasks();
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyInt(), anyInt());
        assertEquals(mockList, answer);
    }

    @Test
    public void testGetTaskById() {
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.getTask(anyString())).thenReturn(mockTask);

        Task answer = taskService.getTaskById(UUID.randomUUID().toString());
        verify(mockTaskRepository, times(1)).getTask(anyString());
        assertEquals(mockTask, answer);
    }

    @Test
    public void testRemoveTask() {
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.removeTask(anyString())).thenReturn(mockTask);

        Task answer = taskService.removeTaskById(UUID.randomUUID().toString());
        verify(mockTaskRepository, times(1)).removeTask(anyString());
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