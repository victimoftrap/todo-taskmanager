package it.sevenbits.backend.taskmanager.web.service.tasks;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.config.settings.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;
import it.sevenbits.backend.taskmanager.web.model.meta.GetTasksMetaData;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateTaskResponse;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Service that validates ID from request and generates responses
 */
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final MetaDataSettings settings;
    private final Verifiable<String> idValidator;
    private final Verifiable<String> statusValidator;
    private final Verifiable<String> sortingOrderValidator;

    /**
     * Create task controller service
     *
     * @param repository            object, that works with database
     * @param idValidator           task ID validator
     * @param statusValidator       task status validator
     * @param sortingOrderValidator sorting order for tasks in list validator
     * @param settings              default settings for service
     */
    public TaskServiceImpl(
            final TaskRepository repository,
            final Verifiable<String> idValidator,
            final Verifiable<String> statusValidator,
            final Verifiable<String> sortingOrderValidator,
            final MetaDataSettings settings) {

        this.repository = repository;
        this.idValidator = idValidator;
        this.statusValidator = statusValidator;
        this.sortingOrderValidator = sortingOrderValidator;
        this.settings = settings;
    }

    /**
     * Choose text value for updated task
     *
     * @param upd request data
     * @param old previous task value
     * @return new text for task
     */
    private String updateTaskText(final UpdateTaskRequest upd, final Task old) {
        return upd.getText() == null
                ? old.getText()
                : upd.getText();
    }

    /**
     * Choose status value for updated task
     *
     * @param upd request data
     * @param old previous task value
     * @return new status for task
     */
    private String updateTaskStatus(final UpdateTaskRequest upd, final Task old) {
        return upd.getStatus() == null
                ? old.getStatus()
                : upd.getStatus();
    }

    /**
     * Choose parameter for receiving tasks
     *
     * @param verifiable validator of requested value
     * @param received   requested value
     * @param preset     default value from settings
     * @param <T>        some type
     * @return chosen param
     */
    private <T> T choosePaginationSetting(final Verifiable<T> verifiable, final T received, final T preset) {
        return verifiable.verify(received) ? received : preset;
    }

    /**
     * Choose page size
     *
     * @param received requested page size
     * @param preset   default page size
     * @param minSize  maximum page size
     * @param maxSize  maximum page size
     * @return chosen size
     */
    private int choosePageSize(final Integer received, final Integer preset, final int minSize, final int maxSize) {
        if (received == null || (received < minSize || received > maxSize)) {
            return preset;
        }
        return received;
    }

    /**
     * Choose current page number
     *
     * @param received   requested page number
     * @param preset     default page number
     * @param pagesCount total pages, count by page size
     * @return chosen page number
     */
    private int choosePageNumber(final Integer received, final Integer preset, final int pagesCount) {
        if (received == null || received < preset) {
            return preset;
        } else if (received > pagesCount) {
            return pagesCount;
        } else {
            return received;
        }
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
    public Task createTask(final String owner, final AddTaskRequest request) {
        return repository.createTask(request.getText(), "inbox", owner);
    }

    @Override
    public GetTasksResponse getTasksByStatus(final String owner, final GetTasksRequest request) {
        String status = choosePaginationSetting(statusValidator, request.getStatus(), settings.getStatus());
        String order = choosePaginationSetting(sortingOrderValidator, request.getOrder(), settings.getOrder());
        int size = choosePageSize(request.getSize(), settings.getSize(),
                settings.getMinPageSize(), settings.getMaxPageSize());

        int totalCount = repository.getCountTasks(status, owner);
        int pagesCount = Math.max((int) Math.ceil((double) totalCount / size), 1);
        int page = choosePageNumber(request.getPage(), settings.getPage(), pagesCount);

        List<Task> tasks = repository.getTasks(owner, status, order, page, size);

        String firstPage = buildUriFor(status, order, 1, size);
        String lastPage = buildUriFor(status, order, pagesCount, size);
        String nextPage = page == pagesCount
                ? null
                : buildUriFor(status, order, page + 1, size);
        String prevPage = page == 1
                ? null
                : buildUriFor(status, order, page - 1, size);
        GetTasksMetaData meta = new GetTasksMetaData(totalCount, page, size, nextPage, prevPage, firstPage, lastPage);
        return new GetTasksResponse(meta, tasks);
    }

    @Override
    public Task getTaskById(final String owner, final String id) {
        if (!idValidator.verify(id) || !idValidator.verify(owner)) {
            return null;
        }
        return repository.getTask(id, owner);
    }

    @Override
    public UpdateTaskResponse updateTaskById(final String owner, final String id, final UpdateTaskRequest request) {
        if (!idValidator.verify(id) || !idValidator.verify(owner)) {
            return null;
        }
        if (request.getStatus() != null && !statusValidator.verify(request.getStatus())) {
            return null;
        }

        Task oldTask = repository.getTask(id, owner);
        if (oldTask == null) {
            return new UpdateTaskResponse("");
        }

        String updText = updateTaskText(request, oldTask);
        String updStatus = updateTaskStatus(request, oldTask);
        Task updated = new Task(
                oldTask.getId(),
                updText,
                updStatus,
                oldTask.getCreatedAt(),
                oldTask.getUpdatedAt(),
                owner
        );
        repository.updateTask(updated.getId(), updated);
        return new UpdateTaskResponse(id);
    }

    @Override
    public Task removeTaskById(final String owner, final String id) {
        if (!idValidator.verify(id) || !idValidator.verify(owner)) {
            return null;
        }
        return repository.removeTask(id, owner);
    }
}
