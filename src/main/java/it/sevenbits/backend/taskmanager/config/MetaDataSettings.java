package it.sevenbits.backend.taskmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Class with default metadata settings
 */
@Component
@PropertySource("classpath:configuration.properties")
public class MetaDataSettings {
    @Value("${default.status}")
    private String status = null;

    @Value("${default.order}")
    private String order = null;

    @Value("${default.start-page}")
    private Integer page = null;

    @Value("${default.page-size}")
    private Integer pageSize = null;

    @Value("${default.min-page-size}")
    private Integer minPageSize = null;

    @Value("${default.max-page-size}")
    private Integer maxPageSize = null;

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
