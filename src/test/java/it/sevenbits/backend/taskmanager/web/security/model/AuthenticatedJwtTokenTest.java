package it.sevenbits.backend.taskmanager.web.security.model;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AuthenticatedJwtTokenTest {
    @Test
    public void testModel() {
        String authenticatedToken = "authenticatedToken";
        GrantedAuthority user = new SimpleGrantedAuthority("USER");
        GrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        AuthenticatedJwtToken token = new AuthenticatedJwtToken(Arrays.asList(user, admin), authenticatedToken);

        assertNotNull(token);
        assertEquals(authenticatedToken, token.getPrincipal());
        assertNull(token.getCredentials());
    }
}