package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidationService;

import it.sevenbits.backend.taskmanager.web.service.TaskControllerService;
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
import java.util.List;

/**
 * Class-mediator that would return data from repository to user
 */
@Controller
@RequestMapping(value = "/tasks")
public class TaskController {
    private final TaskControllerService service;

    /**
     * Create controller by some repository
     *
     * @param repository repository for tasks
     */
    public TaskController(final TaskRepository repository) {
        this.service = new TaskControllerService(repository);
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
        return service.createTask(request);
    }

    /**
     * Call service to get list of tasks by some status
     *
     * @param status status of needed tasks
     * @return ResponseEntity with code of operation
     * * Code 200 - successful operation, all tasks returned
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Task>> getTasksByStatus(
            @RequestParam(
                    value = "status",
                    required = false,
                    defaultValue = "inbox"
            ) final String status) {
        return service.getTasksByStatus(status);
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
        return service.getTaskById(id);
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
        return service.updateTask(id, request);
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
    public ResponseEntity<Void> deleteTask(@PathVariable("id") final String id) {
        return service.deleteTask(id);
    }
}
