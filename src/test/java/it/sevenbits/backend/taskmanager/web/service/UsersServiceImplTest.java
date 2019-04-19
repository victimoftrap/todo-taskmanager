package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.UserRoleValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateUserRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateUserResponse;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;
import it.sevenbits.backend.taskmanager.web.service.users.UsersServiceImpl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    private UsersService usersService;
    private UsersRepository mockRepository;
    private final Verifiable<String> idValidator = new IdValidator();
    private final Verifiable<String> userRoleValidator = new UserRoleValidator();
    private User mockUser;
    private String id;

    @Before
    public void setup() {
        id = UUID.randomUUID().toString();
        mockUser = mock(User.class);
        mockRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(mockRepository, idValidator, userRoleValidator);
    }

    @Test
    public void testFindById() {
        when(mockRepository.findUserById(anyString())).thenReturn(mockUser);

        User answer = usersService.getUserById(id);
        verify(mockRepository, times(1))
                .findUserById(id);

        assertEquals(mockUser, answer);
    }

    @Test
    public void testInvalidIdForSearch() {
        User answer = usersService.getUserById("plumtree");
        assertNull(answer);
    }

    @Test
    public void testGetAllUsers() {
        List<User> mockUsers = mock(List.class);

        when(mockRepository.findAll()).thenReturn(mockUsers);

        List<User> answer = usersService.getAllUsers();
        verify(mockRepository, times(1))
                .findAll();

        assertEquals(mockUsers, answer);
    }

    @Test
    public void testFindByName() {
        when(mockRepository.findUserByName(anyString())).thenReturn(mockUser);

        User answer = usersService.getUserByName("Scott Pilgrim");
        verify(mockRepository, times(1))
                .findUserByName("Scott Pilgrim");

        assertEquals(mockUser, answer);
    }

    @Test
    public void testInvalidNameForSearch() {
        User answer = usersService.getUserByName(null);
        assertNull(answer);
    }

    @Test
    public void testUpdateUser() {
        boolean enabled = true;
        List<String> auths = new ArrayList<>();
        Collections.addAll(auths, "USER", "ADMIN");

        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);

        when(mockUser.getUsername()).thenReturn("Scott");
        when(mockUser.getPassword()).thenReturn("awesome-zombie-duck");
        when(mockRepository.findUserById(anyString())).thenReturn(mockUser);
        when(mockRepository.updateUser(any(User.class))).thenReturn(any(User.class));

        UpdateUserResponse response = usersService.updateUser(id, request);
        verify(mockRepository, times(1))
                .updateUser(any(User.class));

        assertEquals(id, response.getId());
    }

    @Test
    public void testUpdateUserButItNotFound() {
        UpdateUserResponse response = usersService.updateUser(id, mock(UpdateUserRequest.class));
        assertEquals("", response.getId());
    }

    @Test
    public void testUpdateUserButSomeRoleNonValid() {
        boolean enabled = true;
        List<String> auths = new ArrayList<>();
        Collections.addAll(auths, "USER", "DUDE");
        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);

        when(mockRepository.findUserById(anyString())).thenReturn(mockUser);

        UpdateUserResponse response = usersService.updateUser(id, request);

        assertNull(response);
    }
}