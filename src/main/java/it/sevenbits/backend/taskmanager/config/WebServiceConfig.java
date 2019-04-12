package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.web.security.JwtSettings;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskControllerService;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JsonWebTokenService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;
import it.sevenbits.backend.taskmanager.web.service.users.WebUsersService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration, that creates services for application
 */
@Configuration
public class WebServiceConfig {
    /**
     * Create tasks service
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

    /**
     * Create users service
     *
     * @param usersRepository repository with users
     * @return service created by repository
     */
    @Bean
    @Qualifier("usersService")
    public UsersService usersService(final UsersRepository usersRepository) {
        return new WebUsersService(usersRepository);
    }

    @Bean
    @Qualifier(value = "jwtTokenService")
    public JwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonWebTokenService(settings);
    }
}
