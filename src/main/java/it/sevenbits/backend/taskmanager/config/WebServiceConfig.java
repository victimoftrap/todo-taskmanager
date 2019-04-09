package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import it.sevenbits.backend.taskmanager.web.service.TaskControllerService;
import it.sevenbits.backend.taskmanager.web.service.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for creating services
 */
@Configuration
public class WebServiceConfig {
    /**
     * Create service
     *
     * @param taskRepository repository with tasks
     * @param settings       default settings for service
     * @return service with repository and default settings
     */
    @Bean
    @Qualifier("taskService")
    public TaskService taskService(final TaskRepository taskRepository, final MetaDataSettings settings) {
        return new TaskControllerService(taskRepository, settings);
    }
}
