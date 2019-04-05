package it.sevenbits.backend.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Class-DTO for adding task in repository
 */
public class AddTaskRequest {
    @NotBlank
    private final String text;

    /**
     * Create request
     *
     * @param text text description of a task
     */
    @JsonCreator
    public AddTaskRequest(@JsonProperty("text") final String text) {
        this.text = text;
    }

    /**
     * Get text description of a task
     *
     * @return text description
     */
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddTaskRequest that = (AddTaskRequest) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
