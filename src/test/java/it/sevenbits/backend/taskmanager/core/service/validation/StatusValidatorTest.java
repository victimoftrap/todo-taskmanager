package it.sevenbits.backend.taskmanager.core.service.validation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusValidatorTest {
    private Verifiable<String> verifiable;

    @Before
    public void setup() {
        verifiable = new StatusValidator();
    }

    @Test
    public void testValidStatuses() {
        assertTrue(verifiable.verify("inbox"));
        assertTrue(verifiable.verify("done"));
    }

    @Test
    public void testInvalidStatuses() {
        assertFalse(verifiable.verify("not-yet"));
        assertFalse(verifiable.verify(""));
    }
}