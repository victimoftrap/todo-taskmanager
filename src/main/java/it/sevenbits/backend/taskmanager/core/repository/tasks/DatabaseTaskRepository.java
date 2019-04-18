package it.sevenbits.backend.taskmanager.core.repository.tasks;

import it.sevenbits.backend.taskmanager.core.model.Task;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.UUID;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.TimeZone;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
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

    private final String TASK_ID = "id";
    private final String TEXT = "text";
    private final String STATUS = "status";
    private final String CREATED_AT = "createdAt";
    private final String UPDATED_AT = "updatedAt";
    private final String OWNER_ID = "owner";

    /**
     * Create repository
     *
     * @param jdbcOperations class that works with database
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.taskMapper = new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet resultSet, int i) throws SQLException {
                String taskId = resultSet.getString(TASK_ID);
                String taskText = resultSet.getString(TEXT);
                String taskStatus = resultSet.getString(STATUS);
                String creationDate = resultSet.getString(CREATED_AT);
                String updateDate = resultSet.getString(UPDATED_AT);
                String ownerId = resultSet.getString(OWNER_ID);
                return new Task(taskId, taskText, taskStatus, creationDate, updateDate, ownerId);
            }
        };
    }

    @Override
    public Task createTask(final String text, final String status, final String owner) {
        String id = UUID.randomUUID().toString();
        Timestamp createTimestamp = Timestamp.from(Instant.now(Clock.systemUTC()));
        jdbcOperations.update(
                "INSERT INTO tasks VALUES(?,?,?,?,?,?)",
                id, text, status, createTimestamp, createTimestamp, owner
        );
        return new Task(id, text, status, createTimestamp.toString(), createTimestamp.toString(), owner);
    }

    @Override
    public Task getTask(final String taskId, final String owner) {
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, text, status, createdAt, updatedAt, owner FROM tasks WHERE owner=? AND id=?",
                    taskMapper,
                    owner, taskId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Task> getTasks(final String owner, final String status, final String order, final int page, final int size) {
        String ascQuery =
                "SELECT id,text,status,createdAt,updatedAt,owner FROM tasks WHERE owner=? AND status=? ORDER BY createdAt ASC OFFSET ? LIMIT ?";
        String descQuery =
                "SELECT id,text,status,createdAt,updatedAt,owner FROM tasks WHERE owner=? AND status=? ORDER BY createdAt DESC OFFSET ? LIMIT ?";
        String query = "asc".equalsIgnoreCase(order) ? ascQuery : descQuery;
        int offset = (page - 1) * size;

        List<Task> result = jdbcOperations.query(
                query,
                taskMapper,
                owner,
                status,
                offset,
                size
        );
        return Collections.unmodifiableList(result);
    }

    @Override
    public Task removeTask(final String taskId, final String owner) {
        Task toDelete = getTask(taskId, owner);
        jdbcOperations.update(
                "DELETE FROM tasks WHERE owner=? AND id=?",
                owner, taskId
        );
        return toDelete;
    }

    @Override
    public void updateTask(final String taskId, final Task updated) {
        Timestamp updateTimestamp = Timestamp.from(Instant.now(Clock.systemUTC()));
        jdbcOperations.update(
                "UPDATE tasks SET text=?, status=?, updatedAt=? WHERE id=?",
                updated.getText(), updated.getStatus(), updateTimestamp, taskId
        );
    }

    @Override
    public int getCountTasks(final String status, final String owner) {
        Integer count = jdbcOperations.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE owner=? AND status=?",
                (resultSet, i) -> resultSet.getInt(1),
                owner, status
        );
        return Optional
                .ofNullable(count)
                .orElse(0);
    }
}
