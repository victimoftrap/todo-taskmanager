package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidationService;
import it.sevenbits.backend.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.UpdateTaskRequest;

import java.util.List;

/**
 * Service that validates ID from request and generates responses
 */
public class TaskControllerService implements TaskService {
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

    @Override
    public Task createTask(final AddTaskRequest request) {
        return repository.createTask(request.getText(), "inbox");
    }

    @Override
    public List<Task> getTasksByStatus(final String status) {
        return repository.getTasks(status);
    }

    @Override
    public Task getTaskById(final String id) {
        if (!idValidation.verify(id)) {
            return null;
        }
        return repository.getTask(id);
    }

    @Override
    public Task updateTaskById(final String id, final UpdateTaskRequest request) {
        if (!idValidation.verify(id)) {
            return null;
        }

        Task oldTask = repository.getTask(id);
        if (oldTask == null) {
            return new Task(null, null, null, null, null);
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
        return updated;
    }

    @Override
    public Task removeTaskById(final String id) {
        if (!idValidation.verify(id)) {
            return null;
        }
        return repository.removeTask(id);
    }
}
