package it.sevenbits.backend.taskmanager.core.service.validation;

/**
 * Interface for verifying something
 *
 * @param <T>
 */
public interface Verifiable<T> {
    /**
     * Verify input
     *
     * @param param some input
     * @return true if param matches
     */
    boolean verify(T param);
}
