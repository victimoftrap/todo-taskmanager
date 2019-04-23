package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.model.requests.UpdateUserRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.UpdateUserResponse;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsersControllerTest {
    private UsersController usersController;
    private UsersService mockUsersService;
    private User mockUser;
    private String id;

    @Before
    public void setup() {
        mockUsersService = mock(UsersService.class);
        usersController = new UsersController(mockUsersService);
        mockUser = mock(User.class);
        id = UUID.randomUUID().toString();
    }

    @Test
    public void testOkForGetUserById() {
        when(mockUsersService.getUserById(anyString())).thenReturn(mockUser);

        ResponseEntity<User> answer = usersController.getUserById(id);
        verify(mockUsersService, times(1))
                .getUserById(id);

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockUser, answer.getBody());
    }

    @Test
    public void testNotFoundForGetUserById() {
        ResponseEntity<User> answer = usersController.getUserById(id);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    @Test
    public void testOkForGetAllUsers() {
        List<User> mockUsers = mock(List.class);

        when(mockUsersService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> answer = usersController.getAllUsers();
        verify(mockUsersService, times(1))
                .getAllUsers();

        assertEquals(HttpStatus.OK, answer.getStatusCode());
        assertEquals(mockUsers, answer.getBody());
    }

    @Test
    public void testNoContentForUpdateAccount() {
        boolean enabled = true;
        List<String> auths = mock(List.class);
        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);
        UpdateUserResponse response = new UpdateUserResponse(id);

        when(mockUsersService.updateUser(anyString(), any(UpdateUserRequest.class))).thenReturn(response);

        ResponseEntity<Void> answer = usersController.updateUserAccount(id, request);
        verify(mockUsersService, times(1))
                .updateUser(id, request);

        assertEquals(HttpStatus.NO_CONTENT, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    @Test
    public void testBadRequestForUpdateAccount() {
        boolean enabled = true;
        List<String> auths = mock(List.class);
        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);
        UpdateUserResponse response = new UpdateUserResponse("");

        when(mockUsersService.updateUser(anyString(), any(UpdateUserRequest.class))).thenReturn(response);

        ResponseEntity<Void> answer = usersController.updateUserAccount(id, request);
        verify(mockUsersService, times(1))
                .updateUser(id, request);

        assertEquals(HttpStatus.BAD_REQUEST, answer.getStatusCode());
        assertNull(answer.getBody());
    }

    @Test
    public void testNotFoundForUpdateAccount() {
        boolean enabled = true;
        List<String> auths = mock(List.class);
        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);

        ResponseEntity<Void> answer = usersController.updateUserAccount(id, request);
        assertEquals(HttpStatus.NOT_FOUND, answer.getStatusCode());
        assertNull(answer.getBody());
    }
}