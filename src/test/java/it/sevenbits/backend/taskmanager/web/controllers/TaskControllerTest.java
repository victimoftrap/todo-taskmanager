package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.config.settings.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.SortingOrderValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.StatusValidator;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskServiceImpl;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {
    private TaskController taskController;
    private TaskService service;
    private WhoAmIService mockWhoAmIService;
    private TaskRepository mockTaskRepository;
    private MetaDataSettings mockMetaData;
    private User mockUser;
    private Task mockTask;
    private String id;
    private String owner;

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);
        mockMetaData = mock(MetaDataSettings.class);

        service = new TaskServiceImpl(
                mockTaskRepository,
                new IdValidator(),
                new StatusValidator(),
                new SortingOrderValidator(),
                mockMetaData
        );
        mockWhoAmIService = mock(WhoAmIService.class);

        taskController = new TaskController(service, mockWhoAmIService);

        mockUser = mock(User.class);
        mockTask = mock(Task.class);
        id = UUID.randomUUID().toString();
        owner = UUID.randomUUID().toString();
    }

    // 201
    @Test
    public void testTaskSuccessfullyCreated() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.createTask(anyString(), anyString(), anyString())).thenReturn(mockTask);

        ResponseEntity<Void> answer = taskController.createTask("token", new AddTaskRequest("text"));
        verify(mockTaskRepository, times(1))
                .createTask(
                        "text", "inbox", owner
                );

        assertEquals(HttpStatus.CREATED, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testNonValidTaskText() {
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(null);

        ResponseEntity<Void> answer = taskController.createTask("token", new AddTaskRequest(""));

        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testNullTaskText() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.createTask(anyString(), anyString(), anyString())).thenReturn(null);

        ResponseEntity<Void> answer = taskController.createTask("token", new AddTaskRequest(null));

        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
    }

    // 400
    @Test
    public void notFoundUserInfoByTokenWhenCreate() {
        ResponseEntity<Void> answer = taskController.createTask("token", new AddTaskRequest("text"));
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 200
    @Test
    public void testGetAllTasks() {
        List<Task> mockTasks = mock(List.class);

        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(mockTasks);

        ResponseEntity<GetTasksResponse> answer = taskController.getTasksByStatus(
                owner, "inbox", "asc", 1, 12
        );
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNotNull(answer.getBody());
        assertNotNull(answer.getBody().getTasks());
    }

    // 200
    @Test
    public void testGetNullTasks() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);

        ResponseEntity<GetTasksResponse> answer = taskController.getTasksByStatus(
                owner, "inbox", "asc", 1, 12
        );
        verify(mockTaskRepository, times(1))
                .getTasks(anyString(), anyString(), anyString(), anyInt(), anyInt());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNotNull(answer.getBody());
        assertNull(answer.getBody().getTasks());
    }

    // 400
    @Test
    public void notFoundUserInfoByTokenWhenGetTaskList() {
        ResponseEntity<GetTasksResponse> answer = taskController.getTasksByStatus(
                "token", "inbox", "desc", 1, 26
        );
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 200
    @Test
    public void testGetTaskById() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.getTask(anyString(), anyString())).thenReturn(mockTask);

        ResponseEntity<Task> answer = taskController.getTasksById(owner, id);
        verify(mockTaskRepository, times(1))
                .getTask(id, owner);

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockTask, answer.getBody());
    }

    // 400
    @Test
    public void notFoundUserInfoByTokenWhenGetTask() {
        ResponseEntity<Task> answer = taskController.getTasksById("token", "id");
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 404
    @Test
    public void testTaskNotFoundById() {
        String invalidId = "abeceda";

        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);

        ResponseEntity<Task> answer = taskController.getTasksById(owner, invalidId);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 200
    @Test
    public void testTaskSuccessfullyDeleted() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.removeTask(anyString(), anyString())).thenReturn(mockTask);

        ResponseEntity<Void> answer = taskController.removeTask("token", id);
        verify(mockTaskRepository, times(1))
                .removeTask(
                        id, owner
                );

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void notFoundUserInfoByTokenWhenDelete() {
        ResponseEntity<Void> answer = taskController.removeTask("owner", "id");
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 404
    @Test
    public void testTaskNotDeleted() {
        String invalidId = "abeceda";

        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);

        ResponseEntity<Void> answer = taskController.removeTask("token", invalidId);

        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 204
    @Test
    public void testUpdateTask() {
        UpdateTaskRequest request = new UpdateTaskRequest("text", "inbox");

        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTask.getId()).thenReturn(id);
        when(mockTaskRepository.getTask(anyString(), anyString())).thenReturn(mockTask);
        doAnswer(invocationOnMock -> {
            Task argument = invocationOnMock.getArgument(0);
            assertEquals(mockTask, argument);
            return argument;
        }).when(mockTaskRepository).updateTask(id, mockTask);

        ResponseEntity<Void> answer = taskController.updateTask(owner, id, request);
        verify(mockTaskRepository, times(1))
                .getTask(
                        id, owner
                );

        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 404
    @Test
    public void testCannotUpdateNotExistingTask() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);
        when(mockTaskRepository.getTask(anyString(), anyString())).thenReturn(null);

        ResponseEntity<Void> answer = taskController.updateTask(
                "token", id, new UpdateTaskRequest("text", "inbox")
        );
        verify(mockTaskRepository, times(1))
                .getTask(
                        id, owner
                );

        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testUpdateTaskByInvalidId() {
        String invalidId = "hehded";

        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);

        ResponseEntity<Void> answer = taskController.updateTask(
                "token", invalidId, new UpdateTaskRequest("text", "inbox")
        );
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void testUpdateTaskByInvalidStatus() {
        when(mockUser.getId()).thenReturn(owner);
        when(mockWhoAmIService.getCurrentUserInfo(anyString())).thenReturn(mockUser);

        ResponseEntity<Void> answer = taskController.updateTask(
                "token", id, new UpdateTaskRequest("text", "status")
        );
        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    // 400
    @Test
    public void notFoundUserInfoByTokenWhenUpdate() {
        ResponseEntity<Void> answer = taskController.updateTask(
                "token", id, new UpdateTaskRequest("text", "inbox")
        );

        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }
}