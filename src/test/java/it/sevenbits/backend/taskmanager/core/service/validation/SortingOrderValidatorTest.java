package it.sevenbits.backend.taskmanager.core.service.validation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortingOrderValidatorTest {
    private Verifiable<String> verifiable;

    @Before
    public void setup() {
        verifiable = new SortingOrderValidator();
    }

    @Test
    public void testValidSorts() {
        assertTrue(verifiable.verify("asc"));
        assertTrue(verifiable.verify("DESC"));
        assertTrue(verifiable.verify("dEsC"));
    }

    @Test
    public void testInvalidSorts() {
        assertFalse(verifiable.verify("quick"));
        assertFalse(verifiable.verify("glDkjTg"));
    }
}