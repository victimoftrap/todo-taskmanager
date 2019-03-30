package it.sevenbits.backend.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class for running Spring application
 */
@SpringBootApplication
public class Application {
    /**
     * Run task web application
     *
     * @param args command line params
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
