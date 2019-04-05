package it.sevenbits.backend.taskmanager.web.model.requests;

import java.util.Objects;

/**
 * Request for getting tasks from repository
 */
public class GetTasksRequest {
    private String status;
    private String order;
    private Integer page;
    private Integer size;

    /**
     * Create request for getting tasks from repository
     *
     * @param status wanted status of tasks
     * @param order  wanted order of date of tasks
     * @param page   number of page
     * @param size   size of page with tasks
     */
    public GetTasksRequest(final String status, final String order, final Integer page, final Integer size) {
        this.status = status;
        this.order = order;
        this.page = page;
        this.size = size;
    }

    /**
     * @return status of a tasks
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return order for a task
     */
    public String getOrder() {
        return order;
    }

    /**
     * @return page number with tasks
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @return size of a page
     */
    public Integer getSize() {
        return size;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetTasksRequest that = (GetTasksRequest) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(order, that.order) &&
                Objects.equals(page, that.page) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, order, page, size);
    }
}
