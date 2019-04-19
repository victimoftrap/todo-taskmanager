package it.sevenbits.backend.taskmanager.config.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class with default metadata settings
 */
@Component
public final class MetaDataSettings {
    private String status;
    private String order;
    private Integer page;
    private Integer pageSize;
    private Integer minPageSize;
    private Integer maxPageSize;

    /**
     * Read default settings for metadata
     *
     * @param status      status of tasks
     * @param order       order of tasks
     * @param page        start page of task list
     * @param pageSize    default page size
     * @param minPageSize min page size
     * @param maxPageSize max page size
     */
    private MetaDataSettings(
            @Value("${configuration.meta.status}") final String status,
            @Value("${configuration.meta.order}") final String order,
            @Value("${configuration.meta.start-page}") final Integer page,
            @Value("${configuration.meta.page-size}") final Integer pageSize,
            @Value("${configuration.meta.min-page-size}") final Integer minPageSize,
            @Value("${configuration.meta.max-page-size}") final Integer maxPageSize) {
        this.status = status;
        this.order = order;
        this.page = page;
        this.pageSize = pageSize;
        this.minPageSize = minPageSize;
        this.maxPageSize = maxPageSize;
    }

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
