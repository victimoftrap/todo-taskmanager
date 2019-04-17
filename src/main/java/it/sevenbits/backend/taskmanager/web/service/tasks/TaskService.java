package it.sevenbits.backend.taskmanager.web.service.tasks;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.GetTasksRequest;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.GetTasksResponse;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateTaskResponse;

/**
 * Interface for work with tasks in server
 */
public interface TaskService {
    /**
     * Create new task
     *
     * @param owner   ID of a user, that creating this tasks
     * @param request request object with text for task
     * @return created task by request
     */
    Task createTask(String owner, AddTaskRequest request);

    /**
     * Get all user's tasks
     *
     * @param owner   ID of a user, that created this tasks
     * @param request request for getting tasks
     * @return response with tasks list
     */
    GetTasksResponse getTasksByStatus(String owner, GetTasksRequest request);

    /**
     * Get task by his ID
     *
     * @param owner ID of a user, that created this tasks
     * @param id    ID of a task
     * @return requested task or null, if task doesn't exist
     */
    Task getTaskById(String owner, String id);

    /**
     * Update task
     *
     * @param owner   ID of a user, that created this tasks
     * @param id      ID of a task
     * @param request request for updating task
     * @return response about task update
     */
    UpdateTaskResponse updateTaskById(String owner, String id, UpdateTaskRequest request);

    /**
     * Delete task
     *
     * @param owner ID of a user, that created this tasks
     * @param id    ID of a task
     * @return deleted task or null, if task doesn't exist
     */
    Task removeTaskById(String owner, String id);
}
