package it.sevenbits.backend.taskmanager.core.repository.tasks;

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
     * @param owner  ID of user, that creates task
     * @return created task
     */
    Task createTask(String text, String status, String owner);

    /**
     * Get task by his ID
     *
     * @param taskId ID of a task
     * @param owner  ID of a user, that created this task
     * @return task from repository
     */
    Task getTask(String taskId, String owner);

    /**
     * Get all tasks by some status
     *
     * @param owner  ID of a user, that created this tasks
     * @param status wanted task status
     * @param order  wanted order of tasks
     * @param page   page number
     * @param size   size of a page
     * @return list with tasks
     */
    List<Task> getTasks(String owner, String status, String order, int page, int size);

    /**
     * Remove task from repository
     *
     * @param taskId ID of a task
     * @param owner ID of a user, that created this tasks
     * @return value associated with key
     */
    Task removeTask(String taskId, String owner);

    /**
     * Update fields of a task
     *
     * @param taskId  ID of a task
     * @param updated new task
     */
    void updateTask(String taskId, Task updated);

    /**
     * Get count of tasks with status
     *
     * @param owner  ID of a user, that created this tasks
     * @param status status of a tasks
     * @return quantity of tasks
     */
    int getCountTasks(String owner, String status);
}
