package it.sevenbits.backend.taskmanager.web.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.backend.taskmanager.core.model.Task;
import it.sevenbits.backend.taskmanager.web.model.meta.GetTasksMetaData;

import java.util.List;
import java.util.Objects;

/**
 * Response with tasks for tasks request
 */
public class GetTasksResponse {
    @JsonProperty("_meta")
    private GetTasksMetaData meta;
    @JsonProperty("tasks")
    private List<Task> tasks;

    /**
     * Create response for tasks list request
     *
     * @param meta metadata for response
     * @param tasks list with tasks
     */
    public GetTasksResponse(final GetTasksMetaData meta, final List<Task> tasks) {
        this.meta = meta;
        this.tasks = tasks;
    }

    /**
     * Get metadata for tasks request
     *
     * @return metadata for request
     */
    @JsonProperty("_meta")
    public GetTasksMetaData getMeta() {
        return meta;
    }

    /**
     * Get list with task for request
     *
     * @return tasks list
     */
    @JsonProperty("tasks")
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetTasksResponse that = (GetTasksResponse) o;
        return Objects.equals(meta, that.meta) &&
                Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, tasks);
    }
}
