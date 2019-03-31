package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.backend.taskmanager.web.model.UpdateTaskRequest;

import java.util.List;

/**
 * Interface for work with tasks in server
 */
public interface TaskService {
    /**
     * Create new task
     *
     * @param request request object with text for task
     * @return created task by request
     */
    Task createTask(AddTaskRequest request);

    /**
     * Get all user's tasks
     *
     * @param status status of needed tasks
     * @return list with tasks or empty list
     */
    List<Task> getTasksByStatus(String status);

    /**
     * Get task by his ID
     *
     * @param id ID of a task
     * @return requested task or null, if task doesn't exist
     */
    Task getTaskById(String id);

    /**
     * Update task
     *
     * @param id      ID of a task
     * @param request task with updated values
     * @return updated task or null, if task doesn't exist
     */
    Task updateTaskById(String id, UpdateTaskRequest request);

    /**
     * Delete task
     *
     * @param id ID of a task
     * @return deleted task or null, if task doesn't exist
     */
    Task removeTaskById(String id);
}
