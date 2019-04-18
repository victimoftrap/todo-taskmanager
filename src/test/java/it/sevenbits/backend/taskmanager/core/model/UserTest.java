package it.sevenbits.backend.taskmanager.core.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testModel() {
        String id = UUID.randomUUID().toString();
        String name = "name";
        String password = "pass";
        boolean enabled = true;
        List<String> auths = new ArrayList<>();
        auths.add("USER");
        User user = new User(id, name, password, enabled, auths);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(enabled, user.isEnabled());
        assertEquals(auths, user.getAuthorities());
    }
}