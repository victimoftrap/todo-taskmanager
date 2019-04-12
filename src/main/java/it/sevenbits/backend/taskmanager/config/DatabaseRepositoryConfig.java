package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.repository.tasks.DatabaseTaskRepository;
import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.repository.users.DatabaseUsersRepository;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;

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
     * @return repository with tasks
     */
    @Bean
    public TaskRepository tasksRepository(
            @Qualifier("tasksJdbcOperations") final JdbcOperations jdbcOperations) {
        return new DatabaseTaskRepository(jdbcOperations);
    }

    /**
     * Create users repository, connected to database
     *
     * @param jdbcOperations class-wrapper for better work with database
     * @return repository with users
     */
    @Bean
    public UsersRepository usersRepository(
            @Qualifier(value = "tasksJdbcOperations") final JdbcOperations jdbcOperations) {
        return new DatabaseUsersRepository(jdbcOperations);
    }
}
