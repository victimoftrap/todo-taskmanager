package it.sevenbits.backend.taskmanager.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration for connection to database
 */
@Configuration
public class TasksDatabaseConfig {
    /**
     * Get connection pool wrapper for database
     *
     * @return DataSource with connections to database
     */
    @Bean
    @FlywayDataSource
    @Qualifier("tasksDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tasks")
    public DataSource tasksDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Get more comfortable class to work with database
     *
     * @param tasksDataSource class-mediator that would work with database
     * @return class-implementation of JdbcOperations
     */
    @Bean
    @Qualifier("tasksJdbcOperations")
    public JdbcOperations tasksJdbcOperations(@Qualifier("tasksDataSource") final DataSource tasksDataSource) {
        return new JdbcTemplate(tasksDataSource);
    }
}
