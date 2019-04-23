package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of users repository based on connection to database
 */
public class DatabaseUsersRepository implements UsersRepository {
    private JdbcOperations jdbcOperations;
    private final String ID = "id";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String ENABLED = "enabled";
    private final String AUTHORITY = "authority";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_USER = "USER";

    /**
     * Create repository
     *
     * @param jdbcOperations class that works with database
     */
    public DatabaseUsersRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Get authorities for user
     *
     * @param id ID of the user
     * @return list with authorities
     */
    private List<String> getUserAuthorities(final String id) {
        return jdbcOperations.query(
                "SELECT authority FROM authorities WHERE userId = ?",
                (resultSet, i) -> resultSet.getString(AUTHORITY),
                id
        );
    }

    @Override
    public User createUser(final SignUpRequest request) {
        String userQuery = "INSERT INTO users VALUES (?, ?, ?, ?)";
        String authoritiesQuery = "INSERT INTO authorities VALUES (?, ?)";

        String id = UUID.randomUUID().toString();
        String username = request.getUsername();
        String password = request.getPassword();
        boolean enabled = true;
        List<String> authorities = new ArrayList<>();
        authorities.add(ROLE_USER);

        try {
            jdbcOperations.update(
                    userQuery,
                    id, username, password, enabled
            );
        } catch (DuplicateKeyException e) {
            return null;
        }
        jdbcOperations.update(
                authoritiesQuery,
                id, ROLE_USER
        );
        return new User(id, username, password, true, Collections.unmodifiableList(authorities));
    }

    @Override
    public User findUserById(final String id) {
        return findUserById(id, true);
    }

    @Override
    public User findUserById(final String id, final boolean onlyEnabled) {
        final String allQuery = "SELECT id, username, password, enabled FROM users WHERE id = ?";
        final String enabledQuery =
                "SELECT id, username, password, enabled FROM users WHERE enabled = true AND id = ?";
        String query = onlyEnabled ? enabledQuery : allQuery;

        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    query, id
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
        List<String> authorities = getUserAuthorities(id);

        String username = String.valueOf(rawUser.get(USERNAME));
        String password = String.valueOf(rawUser.get(PASSWORD));
        boolean enabled = Boolean.valueOf(String.valueOf(rawUser.get(ENABLED)));
        return new User(id, username, password, enabled, authorities);
    }

    @Override
    public User findUserByName(final String username) {
        return findUserByName(username, true);
    }

    @Override
    public User findUserByName(final String username, final boolean onlyEnabled) {
        final String allQuery = "SELECT id, username, password, enabled FROM users WHERE username = ?";
        final String enabledQuery =
                "SELECT id, username, password, enabled FROM users WHERE enabled = true AND username = ?";
        String query = onlyEnabled ? enabledQuery : allQuery;

        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    query, username
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        String id = String.valueOf(rawUser.get(ID));
        String password = String.valueOf(rawUser.get(PASSWORD));
        boolean enabled = Boolean.valueOf(String.valueOf(rawUser.get(ENABLED)));
        List<String> authorities = getUserAuthorities(id);
        return new User(id, username, password, enabled, authorities);
    }

    @Override
    public List<User> findAll() {
        return findAll(true);
    }

    @Override
    public List<User> findAll(final boolean onlyEnabled) {
        final String allQuery = "SELECT id, username, password, enabled, authority FROM users JOIN authorities ON id = userId";
        final String enabledQuery =
                "SELECT id, username, password, enabled, authority FROM users JOIN authorities ON id = userId WHERE enabled = true";
        String query = onlyEnabled ? enabledQuery : allQuery;

        Map<String, User> users = new HashMap<>();
        jdbcOperations.query(query, new RowMapper<User>() {
            @Override
            public User mapRow(final ResultSet resultSet, final int i) throws SQLException {
                String id = resultSet.getString(ID);
                String username = resultSet.getString(USERNAME);
                String password = resultSet.getString(PASSWORD);
                boolean enabled = resultSet.getBoolean(ENABLED);
                String newRole = resultSet.getString(AUTHORITY);

                User user = users.computeIfAbsent(
                        id,
                        none -> new User(id, username, password, enabled, new ArrayList<>())
                );
                List<String> roles = user.getAuthorities();
                roles.add(newRole);
                return user;
            }
        });
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

    @Override
    public User updateUser(final User updatedAccount) {
        String id = updatedAccount.getId();
        boolean enabled = updatedAccount.isEnabled();
        jdbcOperations.update(
                "UPDATE users SET enabled=? WHERE id=?",
                enabled, id
        );

        List<String> authorities = updatedAccount.getAuthorities();
        if (authorities != null) {
            jdbcOperations.update("DELETE FROM authorities WHERE userId=?", id);
            for (String authority : authorities) {
                jdbcOperations.update(
                        "INSERT INTO authorities VALUES(?, ?)",
                        id, authority
                );
            }
        }
        return updatedAccount;
    }
}
