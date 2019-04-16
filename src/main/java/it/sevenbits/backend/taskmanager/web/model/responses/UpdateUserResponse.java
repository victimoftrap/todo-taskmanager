package it.sevenbits.backend.taskmanager.web.model.responses;

import java.util.Objects;

/**
 * Response after updating user
 */
public class UpdateUserResponse {
    private final String id;

    /**
     * Create response
     *
     * @param id ID of updated user
     */
    public UpdateUserResponse(final String id) {
        this.id = id;
    }

    /**
     * Get ID of updated user
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateUserResponse that = (UpdateUserResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
