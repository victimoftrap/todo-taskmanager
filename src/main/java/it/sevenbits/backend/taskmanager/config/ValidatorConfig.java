package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.service.validation.IdValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.SortingOrderValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.StatusValidator;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration that creates and registers validators for system
 */
@Configuration
public class ValidatorConfig {
    /**
     * Create ID validator of tasks and users
     *
     * @return ID validator
     */
    @Bean
    @Qualifier("idValidator")
    public Verifiable<String> idValidator() {
        return new IdValidator();
    }

    /**
     * Create task status validator
     *
     * @return task validator
     */
    @Bean
    @Qualifier("statusValidator")
    public Verifiable<String> statusValidator() {
        return new StatusValidator();
    }

    /**
     * Create task sorting order validator
     *
     * @return sorting order validator
     */
    @Bean
    @Qualifier("sortingOrderValidator")
    public Verifiable<String> sortingOrderValidator() {
        return new SortingOrderValidator();
    }
}
