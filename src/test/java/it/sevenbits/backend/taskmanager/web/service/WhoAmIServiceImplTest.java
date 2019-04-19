package it.sevenbits.backend.taskmanager.web.service;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WhoAmIServiceImplTest {
    private WhoAmIService whoAmIService;
    private UsersRepository mockUsersRepository;
    private JwtTokenService mockJwtTokenService;
    private User mockUser;

    @Before
    public void setup() {
        mockUsersRepository = mock(UsersRepository.class);
        mockJwtTokenService = mock(JwtTokenService.class);
        whoAmIService = new WhoAmIServiceImpl(mockUsersRepository, mockJwtTokenService);
        mockUser = mock(User.class);
    }

    @Test
    public void testWhoAmI() {
        Authentication mockAuth = mock(Authentication.class);
        Object mockPrincipal = mock(Object.class);
        String name = "name";
        String token = "Bearer name";

        when(mockJwtTokenService.parseToken(anyString())).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(mockPrincipal);
        when(mockPrincipal.toString()).thenReturn(name);
        when(mockUsersRepository.findUserByName(anyString())).thenReturn(mockUser);

        User answer = whoAmIService.getCurrentUserInfo(token);
        verify(mockUsersRepository, times(1))
                .findUserByName(name);

        assertEquals(mockUser, answer);
    }
}