package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;

import it.sevenbits.backend.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.backend.taskmanager.web.service.signin.SignInService;
import it.sevenbits.backend.taskmanager.web.service.signin.SignInServiceImpl;
import org.junit.Before;
import org.junit.Test;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SignInServiceImplTest {
    private SignInService signInService;
    private UsersRepository mockUsersRepository;
    private PasswordEncoder mockPasswordEncoder;
    private User mockUser;
    private String id;

    @Before
    public void setup() {
        id = UUID.randomUUID().toString();
        mockUser = mock(User.class);
        mockUsersRepository = mock(UsersRepository.class);
        mockPasswordEncoder = mock(PasswordEncoder.class);
        signInService = new SignInServiceImpl(mockUsersRepository, mockPasswordEncoder);
    }

    @Test
    public void testSignIn() {
        User user = new User(id, "Scott", "garbage-truck", true, mock(List.class));
        SignInRequest request = new SignInRequest(user.getUsername(), user.getPassword());

        when(mockUsersRepository.findUserByName(anyString())).thenReturn(user);
        when(mockPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        User answer = signInService.signIn(request);
        verify(mockPasswordEncoder, times(1))
                .matches(request.getPassword(), user.getPassword());

        assertEquals(user.getId(), answer.getId());
        assertEquals(user.getUsername(), answer.getUsername());
        assertEquals(user.getPassword(), answer.getPassword());
    }

    @Test
    public void testNotFound() {
        User answer = signInService.signIn(new SignInRequest("user", "pass"));
        assertNull(answer);
    }

    @Test
    public void testPasswordNotMatches() {
        User user = new User(id, "Scott", "garbage-truck", true, mock(List.class));
        SignInRequest request = new SignInRequest(user.getUsername(), user.getPassword());

        when(mockUsersRepository.findUserByName(anyString())).thenReturn(user);
        when(mockPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        User answer = signInService.signIn(request);
        assertNull(answer);
    }
}