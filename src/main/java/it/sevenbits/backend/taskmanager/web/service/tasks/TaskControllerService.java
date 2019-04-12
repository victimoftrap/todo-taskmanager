package it.sevenbits.backend.taskmanager.web.service.tasks;

import it.sevenbits.backend.taskmanager.config.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.SortingOrderValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.StatusValidator;
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
public class TaskControllerService implements TaskService {
    private final TaskRepository repository;
    private final MetaDataSettings settings;
    private final Verifiable<String> idValidation = new IdValidator();
    private final Verifiable<String> statusValidator = new StatusValidator();
    private final Verifiable<String> orderValidator = new SortingOrderValidator();

    /**
     * Create task controller service
     *
     * @param repository object, that works with database
     * @param settings   default settings for service
     */
    public TaskControllerService(final TaskRepository repository, final MetaDataSettings settings) {
        this.repository = repository;
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
    public Task createTask(final AddTaskRequest request) {
        return repository.createTask(request.getText(), "inbox");
    }

    @Override
    public GetTasksResponse getTasksByStatus(final GetTasksRequest request) {
        String status = choosePaginationSetting(statusValidator, request.getStatus(), settings.getStatus());
        String order = choosePaginationSetting(orderValidator, request.getOrder(), settings.getOrder());
        int size = choosePageSize(request.getSize(), settings.getPage(),
                settings.getMinPageSize(), settings.getMaxPageSize());

        int totalCount = repository.getCountTasks(status);
        int pagesCount = Math.max((int) Math.ceil((double) totalCount / size), 1);
        int page = choosePageNumber(request.getPage(), settings.getPage(), pagesCount);

        List<Task> tasks = repository.getTasks(status, order, page, size);

        String firstPage = buildUriFor(status, order, 1, size);
        String lastPage = buildUriFor(status, order, pagesCount, size);
        String nextPage = page == pagesCount
                ? ""
                : buildUriFor(status, order, page + 1, size);
        String prevPage = page == 1
                ? ""
                : buildUriFor(status, order, page - 1, size);
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
    public UpdateTaskResponse updateTaskById(final String id, final UpdateTaskRequest request) {
        if (!idValidation.verify(id)) {
            return null;
        }
        if (request.getStatus() != null && !statusValidator.verify(request.getStatus())) {
            return null;
        }

        Task oldTask = repository.getTask(id);
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
                oldTask.getUpdatedAt()
        );
        repository.updateTask(updated.getId(), updated);
        return new UpdateTaskResponse(id);
    }

    @Override
    public Task removeTaskById(final String id) {
        if (!idValidation.verify(id)) {
            return null;
        }
        return repository.removeTask(id);
    }
}
