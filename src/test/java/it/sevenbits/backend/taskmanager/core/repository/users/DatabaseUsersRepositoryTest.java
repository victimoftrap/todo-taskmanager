package it.sevenbits.backend.taskmanager.core.repository.users;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseUsersRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private UsersRepository usersRepository;
    private User mockUser;
    private String id;

    @Before
    public void setup() {
        id = UUID.randomUUID().toString();
        mockUser = mock(User.class);
        mockJdbcOperations = mock(JdbcOperations.class);
        usersRepository = new DatabaseUsersRepository(mockJdbcOperations);
    }

    @Test
    public void testCreateUser() {
        SignUpRequest request = new SignUpRequest("name", "pass");

        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), anyString(), anyBoolean())).thenReturn(1);
        when(mockJdbcOperations.update(anyString(), anyString(), anyString())).thenReturn(1);

        User response = usersRepository.createUser(request);
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("INSERT INTO users VALUES (?, ?, ?, ?)"),
                        anyString(),
                        eq(request.getUsername()),
                        eq(request.getPassword()),
                        eq(true)
                );
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("INSERT INTO authorities VALUES (?, ?)"),
                        anyString(),
                        eq("USER")
                );
        assertNotNull(response);
        assertEquals(request.getUsername(), response.getUsername());
        assertEquals(request.getPassword(), response.getPassword());
    }

    @Test
    public void testDuplicateUsername() {
        SignUpRequest request = new SignUpRequest("name", "pass");

        when(mockJdbcOperations.update(anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenThrow(new DuplicateKeyException("msg"));

        User response = usersRepository.createUser(request);
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("INSERT INTO users VALUES (?, ?, ?, ?)"),
                        anyString(),
                        eq(request.getUsername()),
                        eq(request.getPassword()),
                        eq(true)
                );
        assertNull(response);
    }

    @Test
    public void testGetUserById() {
        List<User> mockUsers = mock(List.class);
        Map<String, Object> mockRawUser = mock(Map.class);

        when(mockJdbcOperations.queryForMap(anyString(), anyString())).thenReturn(mockRawUser);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(mockUsers);

        User answer = usersRepository.findUserById(id);
        verify(mockJdbcOperations, times(1))
                .queryForMap(
                        eq("SELECT id, username, password, enabled FROM users WHERE enabled = true AND id = ?"),
                        eq(id)
                );
        verify(mockJdbcOperations, times(1))
                .query(
                        eq("SELECT authority FROM authorities WHERE userId = ?"),
                        any(RowMapper.class),
                        eq(id)
                );

        assertNotNull(answer);
        assertEquals(id, answer.getId());
    }

    @Test
    public void testNotFoundUserById() {
        Map<String, Object> mockRawUser = mock(Map.class);

        when(mockJdbcOperations.queryForMap(anyString(), anyString()))
                .thenThrow(new IncorrectResultSizeDataAccessException(1));

        User answer = usersRepository.findUserById(id);
        verify(mockJdbcOperations, times(1))
                .queryForMap(
                        eq("SELECT id, username, password, enabled FROM users WHERE enabled = true AND id = ?"),
                        eq(id)
                );
        assertNull(answer);
    }

    @Test
    public void testGetUserByName() {
        String name = "name";
        Map<String, Object> mockRawUser = mock(Map.class);
        List<User> mockAuths = mock(List.class);

        when(mockRawUser.get("id")).thenReturn(id);
        when(mockJdbcOperations.queryForMap(anyString(), anyString())).thenReturn(mockRawUser);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(mockAuths);

        User answer = usersRepository.findUserByName(name);
        verify(mockJdbcOperations, times(1))
                .queryForMap(
                        eq("SELECT id, username, password, enabled FROM users WHERE enabled = true AND username = ?"),
                        eq(name)
                );
        verify(mockJdbcOperations, times(1))
                .query(
                        eq("SELECT authority FROM authorities WHERE userId = ?"),
                        any(RowMapper.class),
                        eq(id)
                );

        assertNotNull(answer);
        assertEquals(name, answer.getUsername());
    }

    @Test
    public void testNotFoundUserByName() {
        String name = "name";
        Map<String, Object> mockRawUser = mock(Map.class);

        when(mockJdbcOperations.queryForMap(anyString(), anyString()))
                .thenThrow(new IncorrectResultSizeDataAccessException(1));

        User answer = usersRepository.findUserByName(name);
        verify(mockJdbcOperations, times(1))
                .queryForMap(
                        eq("SELECT id, username, password, enabled FROM users WHERE enabled = true AND username = ?"),
                        eq(name)
                );
        assertNull(answer);
    }

    @Test
    public void testSuccessfulUpdate() {
        boolean enabled = true;
        List<String> auths = new ArrayList<>();
        Collections.addAll(auths, "USER", "ADMIN");
        User user = new User(id, "rivers", "asutl", enabled, auths);

        when(mockJdbcOperations.update(anyString(), anyBoolean(), anyString())).thenReturn(1);
        when(mockJdbcOperations.update(anyString(), anyString())).thenReturn(1);
        when(mockJdbcOperations.update(anyString(), anyString(), anyString())).thenReturn(1);

        usersRepository.updateUser(user);
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("UPDATE users SET enabled=? WHERE id=?"),
                        eq(enabled),
                        eq(id)
                );
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("DELETE FROM authorities WHERE userId=?"),
                        eq(id)
                );
        verify(mockJdbcOperations, times(2))
                .update(
                        eq("INSERT INTO authorities VALUES(?, ?)"),
                        eq(id),
                        anyString()
                );
    }

    @Test
    public void testUpdateOnlyEnabledFlag() {
        boolean enabled = true;
        User user = new User(id, "rivers", "asutl", enabled, null);

        when(mockJdbcOperations.update(anyString(), anyBoolean(), anyString())).thenReturn(1);

        usersRepository.updateUser(user);
        verify(mockJdbcOperations, times(1))
                .update(
                        eq("UPDATE users SET enabled=? WHERE id=?"),
                        eq(enabled),
                        eq(id)
                );
    }

    @Test
    public void testFindAllTasks() {
        List<User> mockUsers = mock(List.class);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class))).thenReturn(mockUsers);

        List<User> answer = usersRepository.findAll();
        verify(mockJdbcOperations, times(1))
                .query(
                        eq("SELECT id, username, password, enabled, authority FROM users JOIN authorities ON id = userId WHERE enabled = true"),
                        any(RowMapper.class)
                );
    }
}