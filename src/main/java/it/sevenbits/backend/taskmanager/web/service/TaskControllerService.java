package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidationService;
import it.sevenbits.backend.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.UpdateTaskRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Service that validates requests and generates responses
 */
public class TaskControllerService {
    private final TaskRepository repository;
    private final IdValidationService idValidation = new IdValidationService();

    /**
     * Create task controller service
     *
     * @param repository object, that works with database
     */
    public TaskControllerService(final TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Choose text value for updated task
     *
     * @param upd request data
     * @param old previous task value
     * @return new text for task
     */
    private String updateTaskText(final UpdateTaskRequest upd, final Task old) {
        return upd.getText() == null ? old.getText() : upd.getText();
    }

    /**
     * Choose status value for updated task
     *
     * @param upd request data
     * @param old previous task value
     * @return new status for task
     */
    private String updateTaskStatus(final UpdateTaskRequest upd, final Task old) {
        return upd.getStatus() == null ? old.getStatus() : upd.getStatus();
    }

    /**
     * Create new task
     *
     * @param request request object with text for task
     * @return status code of operation
     * * Code 201 - task created;
     * * Code 400 - request invalid or request text are empty
     */
    public ResponseEntity<Void> createTask(final AddTaskRequest request) {
        Task task = repository.createTask(request.getText(), "inbox");
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
     * Get all user's tasks
     *
     * @param status status of needed tasks
     * @return list with tasks or empty list
     * * Code 200 - successful operation, all tasks returned
     */
    public ResponseEntity<List<Task>> getTasksByStatus(final String status) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(repository.getTasks(status));
    }

    /**
     * Get task by his ID
     *
     * @param id ID of a task
     * @return requested task
     * * Code 200 - successful operation;
     * * Code 404 - task by ID not found
     */
    public ResponseEntity<Task> getTaskById(final String id) {
        if (!idValidation.verify(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Task task = repository.getTask(id);
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
     * Update task
     *
     * @param id      ID of a task
     * @param request task with updated values
     * @return response code of operation
     * * Code 204 - successful operation;
     * * Code 400 - validation exception;
     * * Code 404 - task by ID not found.
     */
    public ResponseEntity<Void> updateTask(final String id, final UpdateTaskRequest request) {
        if (!idValidation.verify(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Task oldTask = repository.getTask(id);
        if (oldTask == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        String updText = updateTaskText(request, oldTask);
        String updStatus = updateTaskStatus(request, oldTask);
        Task updated = new Task(
                oldTask.getId(),
                updText,
                updStatus,
                oldTask.getCreatedAt(),
                oldTask.getUpdatedAt()
        );
        repository.updateTask(updated.getId(), updated);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Delete task
     *
     * @param id ID of a task
     * @return response code of operation
     * * Code 200 - successful operation;
     * * Code 404 - task by ID not found.
     */
    public ResponseEntity<Void> deleteTask(final String id) {
        if (!idValidation.verify(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Task removed = repository.removeTask(id);
        if (removed == null) {
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
