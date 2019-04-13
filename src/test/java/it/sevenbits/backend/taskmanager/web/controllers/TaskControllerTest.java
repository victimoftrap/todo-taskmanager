package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.config.settings.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskControllerService;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {
    private TaskRepository mockTaskRepository;
    private TaskController taskController;
    private MetaDataSettings mockMetaData;
    private TaskService service;

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);
        mockMetaData = mock(MetaDataSettings.class);
        service = new TaskControllerService(mockTaskRepository, mockMetaData);
        taskController = new TaskController(service);
    }

    // 201
    @Test
    public void testTaskSuccessfullyCreated() {
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.createTask(anyString(), anyString())).thenReturn(mockTask);

        ResponseEntity<Void> answer = taskController.createTask(new AddTaskRequest("text"));
        verify(mockTaskRepository, times(1)).createTask(anyString(), anyString());

        assertEquals(HttpStatus.CREATED, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testNonValidTaskText() {
        ResponseEntity<Void> answer = taskController.createTask(new AddTaskRequest(""));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

    // 400
    @Test
    public void testNullTaskText() {
        ResponseEntity<Void> answer = taskController.createTask(new AddTaskRequest(null));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

    // 200
    @Test
    public void testGetAllTasks() {
        List<Task> mockTasks = mock(List.class);
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockTasks);

        ResponseEntity<GetTasksResponse> answer = taskController.getTasksByStatus(
                "inbox", "asc", 1, 12
        );
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyInt(), anyInt());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
    }

    // 200
    @Test
    public void testGetNullTasks() {
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);

        ResponseEntity<GetTasksResponse> answer = taskController.getTasksByStatus(
                "inbox", "asc", 1, 12
        );
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyInt(), anyInt());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNotNull(answer.getBody());
    }

    // 200
    @Test
    public void testGetTaskById() {
        String id = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.getTask(anyString())).thenReturn(mockTask);

        ResponseEntity<Task> answer = taskController.getTasksById(id);
        verify(mockTaskRepository, times(1)).getTask(id);

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockTask, answer.getBody());
    }

    // 404
    @Test
    public void testTaskNotFoundById() {
        String id = "abeceda";
        ResponseEntity<Task> answer = taskController.getTasksById(id);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
    }

    // 200
    @Test
    public void testTaskSuccessfullyDeleted() {
        String id = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockTaskRepository.removeTask(anyString())).thenReturn(mockTask);

        ResponseEntity<Void> answer = taskController.removeTask(id);
        verify(mockTaskRepository, times(1)).removeTask(id);

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 404
    @Test
    public void testTaskNotDeleted() {
        String id = "abeceda";
        ResponseEntity<Void> answer = taskController.removeTask(id);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 204
    @Test
    public void testUpdateTask() {
        String id = UUID.randomUUID().toString();
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(id);
        UpdateTaskRequest request = new UpdateTaskRequest("text", "inbox");

        when(mockTaskRepository.getTask(anyString())).thenReturn(mockTask);
        doAnswer(invocationOnMock -> {
            Task argument = invocationOnMock.getArgument(0);
            assertEquals(mockTask, argument);
            return argument;
        }).when(mockTaskRepository).updateTask(id, mockTask);

        ResponseEntity<Void> answer = taskController.updateTask(id, request);
        verify(mockTaskRepository, times(1)).getTask(id);
        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 404
    @Test
    public void testCannotUpdateNotExistingTask() {
        String id = UUID.randomUUID().toString();
        when(mockTaskRepository.getTask(anyString())).thenReturn(null);

        ResponseEntity<Void> answer = taskController.updateTask(id, new UpdateTaskRequest("text", "inbox"));
        verify(mockTaskRepository, times(1)).getTask(id);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testUpdateTaskByInvalidId() {
        String id = "hehded";

        ResponseEntity<Void> answer = taskController.updateTask(id, new UpdateTaskRequest("text", "inbox"));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    @Test
    public void testUpdateTaskByInvalidStatus() {
        String status = "hehded";

        ResponseEntity<Void> answer = taskController.updateTask(status, new UpdateTaskRequest("text", "status"));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }
}