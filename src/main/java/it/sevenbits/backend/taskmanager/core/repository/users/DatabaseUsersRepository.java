package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Implementation of users repository based on connection to database
 */
public class DatabaseUsersRepository implements UsersRepository {
    private JdbcOperations jdbcOperations;
    private final String ID = "id";
    private final String USERNAME = "username";
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
    public User findUserById(final String id) {
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
        Map<String, User> users = new HashMap<>();

        jdbcOperations.query(query, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                String id = resultSet.getString(ID);
                String username = resultSet.getString(USERNAME);
                String password = resultSet.getString(PASSWORD);
                String newRole = resultSet.getString(AUTHORITY);
                User user = users.computeIfAbsent(id, none -> new User(id, username, password, new ArrayList<>()));
                List<String> roles = user.getAuthorities();
                roles.add(newRole);
                return user;
            }
        });
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }
}
