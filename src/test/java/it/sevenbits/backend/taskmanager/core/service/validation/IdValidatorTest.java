package it.sevenbits.backend.taskmanager.core.service.validation;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class IdValidatorTest {
    private Verifiable<String> verifiable;

    @Before
    public void setup() {
        verifiable = new IdValidator();
    }

    @Test
    public void testValidSorts() {
        assertTrue(verifiable.verify(UUID.randomUUID().toString()));
    }

    @Test
    public void testInvalidSorts() {
        assertFalse(verifiable.verify("123456789"));
    }
}