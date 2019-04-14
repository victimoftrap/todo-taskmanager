package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.core.repository.tasks.TaskRepository;
import it.sevenbits.backend.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskService;
import it.sevenbits.backend.taskmanager.web.service.tasks.TaskControllerService;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;
import it.sevenbits.backend.taskmanager.web.service.users.WebUsersService;
import it.sevenbits.backend.taskmanager.web.service.signin.SignInService;
import it.sevenbits.backend.taskmanager.web.service.signin.WebSignInService;
import it.sevenbits.backend.taskmanager.web.service.signup.SignUpService;
import it.sevenbits.backend.taskmanager.web.service.signup.SignUpServiceImpl;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;
import it.sevenbits.backend.taskmanager.web.service.whoami.WebWhoAmIService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;
import it.sevenbits.backend.taskmanager.web.service.tokens.JsonWebTokenService;
import it.sevenbits.backend.taskmanager.config.settings.JwtSettings;
import it.sevenbits.backend.taskmanager.config.settings.MetaDataSettings;
import it.sevenbits.backend.taskmanager.core.service.validation.Verifiable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration, that creates services for application
 */
@Configuration
public class WebServiceConfig {
    /**
     * Create tasks service
     *
     * @param taskRepository        repository with tasks
     * @param idValidator           task ID validator
     * @param statusValidator       task status validator
     * @param sortingOrderValidator sorting order for tasks in list validator
     * @param settings              default settings for service
     * @return service with repository and default settings
     */
    @Bean
    @Qualifier("taskService")
    public TaskService taskService(
            @Qualifier("tasksRepository") final TaskRepository taskRepository,
            @Qualifier("idValidator") final Verifiable<String> idValidator,
            @Qualifier("statusValidator") final Verifiable<String> statusValidator,
            @Qualifier("sortingOrderValidator") final Verifiable<String> sortingOrderValidator,
            final MetaDataSettings settings) {
        return new TaskControllerService(taskRepository, idValidator, statusValidator, sortingOrderValidator, settings);
    }

    /**
     * Create users service
     *
     * @param usersRepository repository with users
     * @param idValidator     user ID validator
     * @return service created by repository
     */
    @Bean
    @Qualifier("usersService")
    public UsersService usersService(
            @Qualifier("usersRepository") final UsersRepository usersRepository,
            @Qualifier("idValidator") final Verifiable<String> idValidator) {
        return new WebUsersService(usersRepository, idValidator);
    }

    /**
     * Create service for working with JWT tokens
     *
     * @param settings settings for token signature
     * @return token service
     */
    @Bean
    @Qualifier(value = "jwtTokenService")
    public JwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonWebTokenService(settings);
    }

    /**
     * Create service for signing in users
     *
     * @param usersRepository repository with users
     * @param passwordEncoder encoder of a user password
     * @return service that signs in user
     */
    @Bean
    @Qualifier(value = "signInService")
    public SignInService signInService(
            @Qualifier("usersRepository") final UsersRepository usersRepository,
            @Qualifier("passwordEncoder") final PasswordEncoder passwordEncoder) {
        return new WebSignInService(usersRepository, passwordEncoder);
    }

    /**
     * Create service for creating new accounts
     *
     * @param usersRepository repository with users
     * @param passwordEncoder encoder of a user password
     * @return service
     */
    @Bean
    @Qualifier(value = "signUpService")
    public SignUpService signUpService(
            @Qualifier("usersRepository") final UsersRepository usersRepository,
            @Qualifier("passwordEncoder") final PasswordEncoder passwordEncoder) {
        return new SignUpServiceImpl(usersRepository, passwordEncoder);
    }

    /**
     * Create service for recognizing current user
     *
     * @param usersRepository repository with users
     * @param jwtTokenService service that works with user tokens
     * @return information service
     */
    @Bean
    @Qualifier(value = "whoAmIService")
    public WhoAmIService whoAmIService(
            @Qualifier("usersRepository") final UsersRepository usersRepository,
            @Qualifier(value = "jwtTokenService") final JwtTokenService jwtTokenService) {
        return new WebWhoAmIService(usersRepository, jwtTokenService);
    }
}
