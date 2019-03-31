package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of task repository based on connection to database
 */
public class DatabaseTaskRepository implements TaskRepository {
    private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private DateFormat dateFormat;
    private RowMapper<Task> taskMapper;
    private JdbcOperations jdbcOperations;

    /**
     * Create repository
     *
     * @param jdbcOperations class that works with database
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);

        this.taskMapper = (resultSet, i) -> {
            String taskId = resultSet.getString(1);
            String taskText = resultSet.getString(2);
            String taskStatus = resultSet.getString(3);
            String creationDate = resultSet.getString(4);
            String updateDate = resultSet.getString(5);
            return new Task(taskId, taskText, taskStatus, creationDate, updateDate);
        };
    }

    @Override
    public Task createTask(final String text, final String status) {
        String id = UUID.randomUUID().toString();
        String createdAt = dateFormat.format(new Date());
        jdbcOperations.update(
                "INSERT INTO tasks VALUES(?,?,?,?,?)",
                id, text, status, createdAt
        );
        return new Task(id, text, status, createdAt, createdAt);
    }

    @Override
    public Task getTask(final String taskId) {
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE id=?",
                    taskMapper,
                    taskId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Task> getTasks(final String status) {
        return jdbcOperations.query(
                "SELECT id, text, status, createdAt, updatedAt FROM tasks WHERE status=?",
                taskMapper,
                status
        );
    }

    @Override
    public Task removeTask(final String taskId) {
        Task toDelete = getTask(taskId);
        jdbcOperations.update(
                "DELETE FROM tasks WHERE id=?",
                taskId
        );
        return toDelete;
    }

    @Override
    public void updateTask(final String taskId, final Task updated) {
        String updateDate = dateFormat.format(new Date());
        jdbcOperations.update(
                "UPDATE tasks SET text=?, status=?, updatedAt=? WHERE id=?",
                updated.getText(), updated.getStatus(), updateDate, taskId
        );
    }
}
