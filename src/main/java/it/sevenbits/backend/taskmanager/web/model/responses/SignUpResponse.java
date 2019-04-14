package it.sevenbits.backend.taskmanager.web.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Response after creating new account
 */
public class SignUpResponse {
    @JsonProperty("id")
    private final String id;

    /**
     * Create response
     *
     * @param id ID of a created account
     */
    public SignUpResponse(final String id) {
        this.id = id;
    }

    /**
     * Get ID of user
     *
     * @return ID
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
        SignUpResponse that = (SignUpResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
