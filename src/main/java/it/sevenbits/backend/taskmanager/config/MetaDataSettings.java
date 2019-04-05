package it.sevenbits.backend.taskmanager.config;

/**
 * Class with default metadata settings
 */
public class MetaDataSettings {
    private final String status = "inbox";
    private final String order = "desc";
    private final int page = 1;
    private final int pageSize = 25;
    private final int minPageSize = 10;
    private final int maxPageSize = 50;

    /**
     * Get default status of tasks
     *
     * @return default status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get default order for tasks
     *
     * @return default order
     */
    public String getOrder() {
        return order;
    }

    /**
     * Get default page number
     *
     * @return default page number
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Get default page size
     *
     * @return default size
     */
    public Integer getSize() {
        return pageSize;
    }

    /**
     * Get min page size
     *
     * @return min size
     */
    public Integer getMinPageSize() {
        return minPageSize;
    }

    /**
     * Get max page size
     *
     * @return max size
     */
    public Integer getMaxPageSize() {
        return maxPageSize;
    }
}
