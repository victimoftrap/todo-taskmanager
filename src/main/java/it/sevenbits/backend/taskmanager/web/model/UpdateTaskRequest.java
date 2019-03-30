package it.sevenbits.backend.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.backend.taskmanager.core.service.validation.annotations.status.SupportedStatus;

import java.util.Objects;

/**
 * Class-DTO for updating some fields of task in repository
 */
public class UpdateTaskRequest {
    private String text;

    @SupportedStatus
    private String status;

    /**
     * Create request
     *
     * @param text   text description of a task
     * @param status new status of a task
     */
    @JsonCreator
    public UpdateTaskRequest(@JsonProperty("text") final String text,
                             @JsonProperty("status") final String status) {
        this.text = text;
        this.status = status;
    }

    /**
     * Get text description of a task
     *
     * @return text description
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateTaskRequest that = (UpdateTaskRequest) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, status);
    }
}
