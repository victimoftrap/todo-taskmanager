package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;

import java.util.List;

/**
 * Interface for task repository
 */
public interface TaskRepository {
    /**
     * Create new task with some status
     *
     * @param text   text of a task
     * @param status some status for a task
     * @return created task
     */
    Task createTask(String text, String status);

    /**
     * Get task by his ID
     *
     * @param taskId ID of a task
     * @return task from repository
     */
    Task getTask(String taskId);

    /**
     * Get all tasks by some status
     *
     * @param status wanted task status
     * @param order  wanted order of tasks
     * @param page   page number
     * @param size   size of a page
     * @return list with tasks
     */
    List<Task> getTasks(String status, String order, int page, int size);

    /**
     * Remove task from repository
     *
     * @param taskId ID of a task
     * @return value associated with key
     */
    Task removeTask(String taskId);

    /**
     * Update fields of a task
     *
     * @param taskId  ID of a task
     * @param updated new task
     */
    void updateTask(String taskId, Task updated);
}
