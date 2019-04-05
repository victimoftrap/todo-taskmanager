package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.config.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidationService;
import it.sevenbits.backend.taskmanager.web.model.meta.GetTasksMetaData;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * Service that validates ID from request and generates responses
 */
public class TaskControllerService implements TaskService {
    private final TaskRepository repository;
    private final IdValidationService idValidation = new IdValidationService();
    private MetaDataSettings settings = new MetaDataSettings();

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
     * Build URI for task list page
     *
     * @param status status of a tasks
     * @param order  order for list
     * @param page   number of a page
     * @param size   size of a page
     * @return URI
     */
    private String buildUriFor(final String status, final String order, final int page, final int size) {
        return UriComponentsBuilder
                .fromPath("/tasks")
                .queryParam("status", status)
                .queryParam("order", order)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
    }

    @Override
    public Task createTask(final AddTaskRequest request) {
        return repository.createTask(request.getText(), "inbox");
    }

    @Override
    public GetTasksResponse getTasksByStatus(final GetTasksRequest request) {
        String status = Optional.ofNullable(request.getStatus()).orElse(settings.getStatus());
        String order = Optional.ofNullable(request.getOrder()).orElse(settings.getOrder());
        int page = Optional.ofNullable(request.getPage()).orElse(settings.getPage());
        Integer size = request.getSize();
        if (size == null || (size < settings.getMinPageSize() || size > settings.getMaxPageSize())) {
            size = settings.getSize();
        }

        List<Task> tasks = repository.getTasks(status, order, page, size);

        int totalCount = repository.getCountTasks(status);
        int pagesCount = (int) Math.ceil((double) totalCount / size);

        String firstPage = buildUriFor(status, order, 1, size);
        String lastPage = buildUriFor(status, order, pagesCount, size);
        String nextPage = page == pagesCount ? "" : buildUriFor(status, order, page + 1, size);
        String prevPage = page == 1 ? "" : buildUriFor(status, order, page - 1, size);
        GetTasksMetaData meta = new GetTasksMetaData(totalCount, page, size, nextPage, prevPage, firstPage, lastPage);
        return new GetTasksResponse(meta, tasks);
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
