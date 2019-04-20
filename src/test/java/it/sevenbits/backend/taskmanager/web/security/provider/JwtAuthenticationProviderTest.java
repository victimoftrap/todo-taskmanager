package it.sevenbits.backend.taskmanager.web.security.provider;

import it.sevenbits.backend.taskmanager.web.security.model.AuthenticatedJwtToken;
import it.sevenbits.backend.taskmanager.web.security.model.NonAuthenticatedJwtToken;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthenticationProviderTest {
    private JwtAuthenticationProvider provider;
    private JwtTokenService mockJwtTokenService;

    @Before
    public void setup() {
        mockJwtTokenService = mock(JwtTokenService.class);
        provider = new JwtAuthenticationProvider(mockJwtTokenService);
    }

    @Test
    public void testAreAuthenticationSupports() {
        assertFalse(provider.supports(AuthenticatedJwtToken.class));
        assertTrue(provider.supports(NonAuthenticatedJwtToken.class));
    }

    @Test
    public void testAuthenticateToken() {
        String authenticatedToken = "authenticatedToken";
        String nonAuthenticatedToken = "nonAuthenticatedToken";
        GrantedAuthority user = new SimpleGrantedAuthority("USER");
        GrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        AuthenticatedJwtToken authenticatedJwtToken = new AuthenticatedJwtToken(Arrays.asList(user, admin), authenticatedToken);
        NonAuthenticatedJwtToken nonAuthenticatedJwtToken= new NonAuthenticatedJwtToken(nonAuthenticatedToken);

        when(mockJwtTokenService.parseToken(anyString())).thenReturn(authenticatedJwtToken);

        Authentication authentication = provider.authenticate(nonAuthenticatedJwtToken);
        verify(mockJwtTokenService, times(1))
                .parseToken(nonAuthenticatedToken);

        assertNotNull(authentication);
        assertEquals(authenticatedJwtToken, authentication);
    }

    @Test(expected = Exception.class)
    public void testInvalidAuthenticationToken() {
        String nonAuthenticatedToken = "nonAuthenticatedToken";
        NonAuthenticatedJwtToken nonAuthenticatedJwtToken= new NonAuthenticatedJwtToken(nonAuthenticatedToken);

        when(mockJwtTokenService.parseToken(anyString())).thenThrow(new Exception());
        provider.authenticate(nonAuthenticatedJwtToken);
    }
}