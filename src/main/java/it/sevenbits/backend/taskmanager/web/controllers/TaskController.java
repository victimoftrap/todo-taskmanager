package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateTaskResponse;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * Create controller by his service
     *
     * @param service service for tasks controller
     */
    public TaskController(final TaskService service) {
        this.service = service;
    }

    /**
     * Call service to create new task
     *
     * @param request request from user with data for task
     * @return ResponseEntity with code of operation
     * * Code 201 - task created;
     * * Code 400 - request invalid or request text are empty
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<Void> createTask(@RequestBody @Valid final AddTaskRequest request) {
        Task task = service.createTask(request);
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
     * @param status status of needed tasks
     * @param order  wanted order of tasks
     * @param page   number of current page
     * @param size   size of page with tasks
     * @return list with tasks
     * * Code 200 - successful operation, all tasks returned
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GetTasksResponse> getTasksByStatus(
            @RequestParam(value = "status", required = false) final String status,
            @RequestParam(value = "order", required = false) final String order,
            @RequestParam(value = "page", required = false) final Integer page,
            @RequestParam(value = "size", required = false) final Integer size
    ) {
        GetTasksResponse response = service.getTasksByStatus(new GetTasksRequest(status, order, page, size));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response);
    }

    /**
     * Call service to get task by ID
     *
     * @param id ID of a task in repository
     * @return ResponseEntity with requested task
     * * Code 200 - successful operation;
     * * Code 404 - task by ID not found
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Task> getTasksById(@PathVariable("id") final String id) {
        Task task = service.getTaskById(id);
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
     * @param id      ID of a task
     * @param request new data for task
     * @return ResponseEntity with code of operation
     * * Code 204 - successful operation;
     * * Code 400 - validation exception;
     * * Code 404 - task by ID not found.
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PATCH
    )
    @ResponseBody
    public ResponseEntity<Void> updateTask(@PathVariable("id") final String id,
                                           @RequestBody @Valid final UpdateTaskRequest request) {
        UpdateTaskResponse response = service.updateTaskById(id, request);
        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }
        if (response.getId().isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Call service to delete task from repository
     *
     * @param id ID of a task
     * @return ResponseEntity with code of operation
     * * Code 200 - successful operation;
     * * Code 404 - task by ID not found.
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<Void> removeTask(@PathVariable("id") final String id) {
        Task task = service.removeTaskById(id);
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
