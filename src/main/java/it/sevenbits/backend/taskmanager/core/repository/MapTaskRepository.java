package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Basic implementation of TaskRepository
 */
public class MapTaskRepository implements TaskRepository {
    private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private DateFormat dateFormat;
    private Map<String, Task> tasks;
    private Comparator<Task> ascComparator;
    private Comparator<Task> descComparator;

    /**
     * Create repository
     *
     * @param tasksContainer some map container for tasks
     */
    public MapTaskRepository(final Map<String, Task> tasksContainer) {
        tasks = tasksContainer;
        dateFormat = new SimpleDateFormat(DATE_FORMAT);

        ascComparator = (t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt());
        descComparator = (t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt());
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
    public List<Task> getTasks(final String status, final String order, final int page, final int size) {
        int offset = (page - 1) * size;
        List<Task> result = new ArrayList<>();

        int i = 0;
        for (Map.Entry<String, Task> stringTaskEntry : tasks.entrySet()) {
            if (i == offset + size) {
                break;
            }
            if (i >= offset) {
                Task task = stringTaskEntry.getValue();
                if (task.getStatus().equals(status)) {
                    result.add(task);
                }
            }
            i++;
        }
        result.sort("asc".equals(order) ? ascComparator : descComparator);
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

    @Override
    public int getCountTasks(final String status) {
        int count = (int) tasks
                .values()
                .stream()
                .filter(t -> status.equals(t.getStatus()))
                .count();
        return count;
    }
}
