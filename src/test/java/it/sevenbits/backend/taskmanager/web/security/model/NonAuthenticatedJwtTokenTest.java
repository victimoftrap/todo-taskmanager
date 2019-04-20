package it.sevenbits.backend.taskmanager.web.security.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class NonAuthenticatedJwtTokenTest {
    @Test
    public void testModel() {
        String nonAuthenticatedToken = "nonAuthenticatedToken";
        NonAuthenticatedJwtToken token = new NonAuthenticatedJwtToken(nonAuthenticatedToken);

        assertNotNull(token);
        assertEquals(nonAuthenticatedToken, token.getCredentials());
        assertNull(token.getPrincipal());
    }

    @Test
    public void testSetAuthenticated() {
        String nonAuthenticatedToken = "nonAuthenticatedToken";
        NonAuthenticatedJwtToken token = new NonAuthenticatedJwtToken(nonAuthenticatedToken);

        assertFalse(token.isAuthenticated());
        token.setAuthenticated(false);
        assertFalse(token.isAuthenticated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAuthenticate() {
        String nonAuthenticatedToken = "nonAuthenticatedToken";
        NonAuthenticatedJwtToken token = new NonAuthenticatedJwtToken(nonAuthenticatedToken);

        token.setAuthenticated(true);
    }
}