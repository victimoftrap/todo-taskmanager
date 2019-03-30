package it.sevenbits.backend.taskmanager.core.repository;

import it.sevenbits.backend.taskmanager.core.model.Task;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of task repository based on connection to database
 */
public class DatabaseTaskRepository implements TaskRepository {
    private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private JdbcOperations jdbcOperations;

    /**
     * Create repository
     *
     * @param jdbcOperations class that works with database
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Convert ResultSet value to Task
     *
     * @param resultSet result set with data from database
     * @return Task for data from ResultSet
     * @throws SQLException if some troubles with ResultSet happen
     */
    private Task getTaskFromResultSet(final ResultSet resultSet) throws SQLException {
        String taskId = resultSet.getString(1);
        String text = resultSet.getString(2);
        String status1 = resultSet.getString(3);
        String creationDate = resultSet.getString(4);
        return new Task(taskId, text, status1, creationDate);
    }

    @Override
    public Task createTask(final String text, final String status) {
        String id = UUID.randomUUID().toString();
        String createdAt = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        jdbcOperations.update(
                "INSERT INTO tasks VALUES(?,?,?,?)",
                id, text, status, createdAt
        );
        return new Task(id, text, status, createdAt);
    }

    @Override
    public Task getTask(final String taskId) {
        return jdbcOperations.queryForObject(
                "SELECT id, text, status, createdAt FROM tasks WHERE id=?",
                (resultSet, i) -> getTaskFromResultSet(resultSet),
                taskId
        );
    }

    @Override
    public List<Task> getTasks(final String status) {
        return jdbcOperations.query(
                "SELECT id, text, status, createdAt FROM tasks",
                (resultSet, i) -> getTaskFromResultSet(resultSet)
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
        jdbcOperations.update(
                "UPDATE tasks SET text=?, status=? WHERE id=?",
                updated.getText(), updated.getStatus(), taskId
        );
    }
}
