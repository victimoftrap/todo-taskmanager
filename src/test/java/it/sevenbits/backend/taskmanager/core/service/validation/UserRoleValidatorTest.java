package it.sevenbits.backend.taskmanager.core.service.validation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserRoleValidatorTest {
    private Verifiable<String> verifiable;

    @Before
    public void setup() {
        verifiable = new UserRoleValidator();
    }

    @Test
    public void testValidRoles() {
        assertTrue(verifiable.verify("USER"));
        assertTrue(verifiable.verify("ADMIN"));
        assertTrue(verifiable.verify("ANONYMOUS"));
    }

    @Test
    public void testInvalidRoles() {
        assertFalse(verifiable.verify("SUPER-DUPER"));
    }
}