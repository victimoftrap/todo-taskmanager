package it.sevenbits.backend.taskmanager.web.model.meta;

import java.util.Objects;

/**
 * Class that contains metadata for request for list of a tasks
 */
public class GetTasksMetaData {
    private int total;
    private int page;
    private int size;
    private String next;
    private String prev;
    private String first;
    private String last;

    /**
     * Create metadata for request
     *
     * @param total total count of tasks by status
     * @param page  number of current page
     * @param size  size of a page
     * @param next  link to the next page
     * @param prev  link to the previous page
     * @param first link to the first page
     * @param last  link to the last page
     */
    public GetTasksMetaData(final int total, final int page, final int size,
                            final String next, final String prev,
                            final String first, final String last) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.next = next;
        this.prev = prev;
        this.first = first;
        this.last = last;
    }

    /**
     * Get number of all tasks in database for user
     *
     * @return number of a tasks
     */
    public int getTotal() {
        return total;
    }

    /**
     * Get number of current page with tasks
     *
     * @return number of a page
     */
    public int getPage() {
        return page;
    }

    /**
     * Get size of page with tasks
     *
     * @return size of a page
     */
    public int getSize() {
        return size;
    }

    /**
     * Get link to next page with tasks
     *
     * @return string with link to next page
     */
    public String getNext() {
        return next;
    }

    /**
     * Get link to previous page with tasks
     *
     * @return string with link to previous page
     */
    public String getPrev() {
        return prev;
    }

    /**
     * Get link to first page with tasks
     *
     * @return string with link to first page
     */
    public String getFirst() {
        return first;
    }

    /**
     * Get link to last page with tasks
     *
     * @return string with link to last page
     */
    public String getLast() {
        return last;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetTasksMetaData that = (GetTasksMetaData) o;
        return total == that.total &&
                page == that.page &&
                size == that.size &&
                Objects.equals(next, that.next) &&
                Objects.equals(prev, that.prev) &&
                Objects.equals(first, that.first) &&
                Objects.equals(last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, page, size, next, prev, first, last);
    }
}
