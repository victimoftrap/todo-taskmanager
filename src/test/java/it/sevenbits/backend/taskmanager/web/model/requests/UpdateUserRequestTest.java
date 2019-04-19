package it.sevenbits.backend.taskmanager.web.model.requests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UpdateUserRequestTest {
    @Test
    public void testRequest() {
        boolean enabled = false;
        List<String> auths = new ArrayList<>();
        auths.add("USER");
        UpdateUserRequest request = new UpdateUserRequest(enabled, auths);

        assertNotNull(request);
        assertEquals(enabled, request.isEnabled());
        assertEquals(auths, request.getAuthorities());
    }
}