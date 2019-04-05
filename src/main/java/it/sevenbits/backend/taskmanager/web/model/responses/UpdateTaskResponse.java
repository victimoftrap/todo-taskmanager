package it.sevenbits.backend.taskmanager.web.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Response after updating task
 */
public class UpdateTaskResponse {
    @JsonProperty("id")
    private final String id;

    /**
     * Create response
     *
     * @param id ID of updated task
     */
    public UpdateTaskResponse(final String id) {
        this.id = id;
    }

    /**
     * Get ID of updated task
     *
     * @return ID
     */
    @JsonProperty("id")
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
        UpdateTaskResponse that = (UpdateTaskResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
