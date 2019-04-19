package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.backend.taskmanager.web.model.responses.SignUpResponse;
import it.sevenbits.backend.taskmanager.web.service.signup.SignUpService;
import it.sevenbits.backend.taskmanager.web.service.signup.SignUpServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SignUpServiceImplTest {
    private SignUpService signUpService;
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
        signUpService = new SignUpServiceImpl(mockUsersRepository, mockPasswordEncoder);
    }

    @Test
    public void testCreateNewUser() {
        String username = "beazy";
        String password = "sleazy";
        String securePassword = "I now it's getting late";
        SignUpRequest request = new SignUpRequest(username, password);

        when(mockPasswordEncoder.encode(anyString())).thenReturn(securePassword);
        when(mockUsersRepository.createUser(any(SignUpRequest.class))).thenReturn(mockUser);

        SignUpResponse response = signUpService.createAccount(request);
        verify(mockPasswordEncoder, times(1))
                .encode(password);
        verify(mockUsersRepository, times(1))
                .createUser(any(SignUpRequest.class));

        assertNotNull(response);
    }

    @Test
    public void testUserWithRequestedNameExists() {
        String username = "beazy";
        String password = "sleazy";
        String securePassword = "I now it's getting late";
        SignUpRequest request = new SignUpRequest(username, password);

        when(mockPasswordEncoder.encode(anyString())).thenReturn(securePassword);
        when(mockUsersRepository.createUser(any(SignUpRequest.class))).thenReturn(null);

        SignUpResponse response = signUpService.createAccount(request);
        verify(mockPasswordEncoder, times(1))
                .encode(password);
        verify(mockUsersRepository, times(1))
                .createUser(any(SignUpRequest.class));

        assertNull(response);
    }
}