package it.sevenbits.backend.taskmanager.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

import it.sevenbits.backend.taskmanager.core.service.validation.annotations.id.ValidID;
import it.sevenbits.backend.taskmanager.core.service.validation.annotations.status.SupportedStatus;

import java.util.Objects;

/**
 * Class for user's task
 */
public class Task {
    @ValidID
    private final String id;

    @NotBlank
    private final String text;

    @NotBlank
    @SupportedStatus
    private final String status;

    @NotBlank
    private final String createdAt;

    /**
     * Create task
     *
     * @param id        ID of a task
     * @param text      description of a task
     * @param status    status of a task
     * @param createdAt creation date of a task
     */
    @JsonCreator
    public Task(@JsonProperty("id") final String id,
                @JsonProperty("text") final String text,
                @JsonProperty("status") final String status,
                @JsonProperty("createdAt") final String createdAt) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Get ID of a task
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get user description for this task
     *
     * @return text user description
     */
    public String getText() {
        return text;
    }

    /**
     * Get status of this task
     *
     * @return task status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get date of a task creation
     *
     * @return string with date
     */
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(text, task.text) &&
                Objects.equals(status, task.status) &&
                Objects.equals(createdAt, task.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, status, createdAt);
    }
}
