package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of users repository based on connection to database
 */
public class DatabaseUsersRepository implements UsersRepository {
    private JdbcOperations jdbcOperations;
    private final String ID = "id";
    private final String USERNAME = "user";
    private final String PASSWORD = "password";
    private final String AUTHORITY = "authority";

    /**
     * Create repository
     *
     * @param jdbcOperations class that works with database
     */
    public DatabaseUsersRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User findUserById(String id) {
        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users WHERE enabled = true AND id = ?",
                    id
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        List<String> authorities = new ArrayList<>();
        jdbcOperations.query(
                "SELECT authority FROM authorities WHERE userId = ?",
                resultSet -> {
                    String authority = resultSet.getString(AUTHORITY);
                    authorities.add(authority);
                },
                id
        );

        String username = String.valueOf(rawUser.get(USERNAME));
        String password = String.valueOf(rawUser.get(PASSWORD));
        return new User(id, username, password, authorities);
    }

    @Override
    public User findUserByName(final String username) {
        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT id, username, password FROM users WHERE enabled = true AND username = ?",
                    username
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        String id = String.valueOf(rawUser.get(ID));
        String password = String.valueOf(rawUser.get(PASSWORD));

        List<String> authorities = jdbcOperations.query(
                "SELECT authority FROM authorities WHERE userId = ?",
                (resultSet, i) -> resultSet.getString(AUTHORITY),
                id
        );
        return new User(id, username, password, authorities);
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT id, username, password, authority FROM users JOIN authorities ON id = userId WHERE enabled = true";
        Map<String, Object> queryResult = jdbcOperations.queryForMap(query);
        Map<String, User> users = new HashMap<>();
        return null;
    }
}
