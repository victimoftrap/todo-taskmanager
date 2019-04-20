package it.sevenbits.backend.taskmanager.web.service.tokens;

import it.sevenbits.backend.taskmanager.config.settings.JwtSettings;
import it.sevenbits.backend.taskmanager.core.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

public class JsonWebTokenServiceTest {
    private JwtTokenService tokenService;
    private JwtSettings jwtSettings;
    private User user;

    @Before
    public void setup() {
        jwtSettings = new JwtSettings("someuser", "sZcps3y00xnFgpKs8rgoc6lri8uL4mqb1hJ", 30);
        tokenService = new JsonWebTokenService(jwtSettings);
        user = new User(
                UUID.randomUUID().toString(),
                "user", "ilovebees",
                true, Arrays.asList("USER")
        );
    }

    @Test
    public void testCreateToken() {
        String token = tokenService.createToken(user);
        assertNotNull(token);
    }

    @Test
    public void testParseToken() {
        String token = tokenService.createToken(user);
        Authentication authentication = tokenService.parseToken(token);

        assertNotNull(authentication);
        assertEquals(user.getUsername(), authentication.getPrincipal());
    }

    @Test
    public void testExpiredIn() {
        assertEquals(jwtSettings.getTokenExpiredIn(), tokenService.getTokenExpiredIn());
    }
}