package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.repository.DatabaseTaskRepository;
import it.sevenbits.backend.taskmanager.core.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Configuration for work with database
 */
@Configuration
public class DatabaseRepositoryConfig {

    /**
     * Create repository that would work with database
     *
     * @param jdbcOperations repository, connected to database
     * @return repository
     */
    @Bean
    public TaskRepository tasksRepository(
            @Qualifier("tasksJdbcOperations") final JdbcOperations jdbcOperations) {
        return new DatabaseTaskRepository(jdbcOperations);
    }
}
