package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;

import java.text.DateFormat;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Basic implementation of TaskRepository
 */
public class MapTaskRepository implements TaskRepository {
    private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private DateFormat dateFormat;
    private Map<String, Task> tasks;

    /**
     * Create repository
     *
     * @param tasksContainer some map container for tasks
     */
    public MapTaskRepository(final Map<String, Task> tasksContainer) {
        tasks = tasksContainer;
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public Task createTask(final String text, final String status) {
        String id = UUID.randomUUID().toString();
        String createdAt = dateFormat.format(new Date());
        Task newTask = new Task(id, text, status, createdAt, createdAt);

        tasks.putIfAbsent(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public Task getTask(final String taskId) {
        return tasks.get(taskId);
    }

    @Override
    public List<Task> getTasks(final String status) {
        List<Task> result = new ArrayList<>();
        for (Map.Entry<String, Task> stringTaskEntry : tasks.entrySet()) {
            Task task = stringTaskEntry.getValue();
            if (task.getStatus().equals(status)) {
                result.add(task);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public Task removeTask(final String taskId) {
        return tasks.remove(taskId);
    }

    @Override
    public void updateTask(final String taskId, final Task updated) {
        tasks.replace(taskId, updated);
    }
}
