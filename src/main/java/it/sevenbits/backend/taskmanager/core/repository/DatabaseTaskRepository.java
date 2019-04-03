package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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
        Timestamp createTimestamp = Timestamp.from(Instant.now(Clock.systemUTC()));
        jdbcOperations.update(
                "INSERT INTO tasks VALUES(?,?,?,?,?)",
                id, text, status, createTimestamp, createTimestamp
        );
        return new Task(id, text, status, createTimestamp.toString(), createTimestamp.toString());
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
    public List<Task> getTasks(final String status, final String order, final int page, final int size) {
        String ascQuery =
                "SELECT id,text,status,createdAt,updatedAt FROM tasks WHERE status=? ORDER BY createdAt ASC OFFSET ? LIMIT ?";
        String descQuery =
                "SELECT id,text,status,createdAt,updatedAt FROM tasks WHERE status=? ORDER BY createdAt DESC OFFSET ? LIMIT ?";
        String query = "asc".equalsIgnoreCase(order) ? ascQuery : descQuery;
        int offset = (page - 1) * size;

        List<Task> result = jdbcOperations.query(
                query,
                taskMapper,
                status,
                offset,
                size
        );
        return Collections.unmodifiableList(result);
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
        Timestamp createTimestamp = Timestamp.from(Instant.now(Clock.systemUTC()));
        jdbcOperations.update(
                "UPDATE tasks SET text=?, status=?, updatedAt=? WHERE id=?",
                updated.getText(), updated.getStatus(), createTimestamp, taskId
        );
    }

    @Override
    public int getCountTasks(final String status) {
        Integer count = jdbcOperations.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE status=?",
                (resultSet, i) -> resultSet.getInt(1),
                status
        );
        return Optional
                .ofNullable(count)
                .orElse(0);
    }
}
