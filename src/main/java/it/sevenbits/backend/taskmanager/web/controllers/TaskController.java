package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateTaskResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

/**
 * Class-mediator that would return data from repository to user
 */
@Controller
@RequestMapping(value = "/tasks")
public class TaskController {
    private final TaskService service;
    private final WhoAmIService whoAmIService;

    /**
     * Create controller by his service
     *
     * @param service       service for tasks controller
     * @param whoAmIService service that recognize user
     */
    public TaskController(final TaskService service, final WhoAmIService whoAmIService) {
        this.service = service;
        this.whoAmIService = whoAmIService;
    }

    /**
     * Call service to create new task
     *
     * @param token   token of current user
     * @param request request from user with data for task
     * @return ResponseEntity with code of operation
     * 201 - task created;
     * 400 - request invalid or request text are empty
     * 403 - forbidden
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<Void> createTask(@RequestHeader(value = "Authorization") final String token,
                                           @RequestBody @Valid final AddTaskRequest request) {
        User user = whoAmIService.getCurrentUserInfo(token);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        Task task = service.createTask(user.getId(), request);
        if (task == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        URI location = UriComponentsBuilder
                .fromPath("/tasks/")
                .path(task.getId())
                .build()
                .toUri();

        return ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Call service to get list of tasks by some status
     *
     * @param token  token of current user
     * @param status status of needed tasks
     * @param order  wanted order of tasks
     * @param page   number of current page
     * @param size   size of page with tasks
     * @return list with tasks
     * 200 - successful operation, all tasks returned
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GetTasksResponse> getTasksByStatus(
            @RequestHeader(value = "Authorization") final String token,
            @RequestParam(value = "status", required = false) final String status,
            @RequestParam(value = "order", required = false) final String order,
            @RequestParam(value = "page", required = false) final Integer page,
            @RequestParam(value = "size", required = false) final Integer size) {
        User user = whoAmIService.getCurrentUserInfo(token);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        GetTasksResponse response = service.getTasksByStatus(user.getId(), new GetTasksRequest(status, order, page, size));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response);
    }

    /**
     * Call service to get task by ID
     *
     * @param token token of current user
     * @param id    ID of a task in repository
     * @return ResponseEntity with requested task
     * 200 - successful operation;
     * 404 - task by ID not found
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Task> getTasksById(@RequestHeader(value = "Authorization") final String token,
                                             @PathVariable(value = "id") final String id) {
        User user = whoAmIService.getCurrentUserInfo(token);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        Task task = service.getTaskById(user.getId(), id);
        if (task == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(task);
    }

    /**
     * Call service to update task by ID
     *
     * @param token   token of current user
     * @param id      ID of a task
     * @param request new data for task
     * @return ResponseEntity with code of operation
     * 204 - successful operation;
     * 400 - validation exception;
     * 404 - task by ID not found.
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PATCH
    )
    @ResponseBody
    public ResponseEntity<Void> updateTask(@RequestHeader(value = "Authorization") final String token,
                                           @PathVariable(value = "id") final String id,
                                           @RequestBody @Valid final UpdateTaskRequest request) {
        User user = whoAmIService.getCurrentUserInfo(token);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        UpdateTaskResponse response = service.updateTaskById(user.getId(), id, request);
        if (response == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }
        if (response.getId().isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Call service to delete task from repository
     *
     * @param token token of current user
     * @param id    ID of a task
     * @return ResponseEntity with code of operation
     * 200 - successful operation;
     * 404 - task by ID not found.
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<Void> removeTask(@RequestHeader(value = "Authorization") final String token,
                                           @PathVariable(value = "id") final String id) {
        User user = whoAmIService.getCurrentUserInfo(token);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        Task task = service.removeTaskById(user.getId(), id);
        if (task == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }
}
