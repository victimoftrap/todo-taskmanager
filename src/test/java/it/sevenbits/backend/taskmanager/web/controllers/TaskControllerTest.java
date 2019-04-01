package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.web.model.AddTaskRequest;
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

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);
        taskController = new TaskController(mockTaskRepository);
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
        when(mockTaskRepository.getTasks(anyString())).thenReturn(mockTasks);

        ResponseEntity<List<Task>> answer = taskController.getTasksByStatus(anyString());
        verify(mockTaskRepository, times(1)).getTasks(anyString());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockTasks, answer.getBody());
    }

    // 200
    @Test
    public void testGetNullTasks() {
        when(mockTaskRepository.getTasks(anyString())).thenReturn(null);

        ResponseEntity<List<Task>> answer = taskController.getTasksByStatus(anyString());
        verify(mockTaskRepository, times(1)).getTasks(anyString());

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertNull(answer.getBody());
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
}