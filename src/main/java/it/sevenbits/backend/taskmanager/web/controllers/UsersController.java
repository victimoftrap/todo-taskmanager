package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.service.users.UsersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Class-mediator that would return data from users service to user with special authorities
 */
@RestController
@RequestMapping(value = "/users")
public class UsersController {
    private final UsersService service;

    /**
     * Create users controller by users service
     *
     * @param service service that validates requests
     */
    public UsersController(final UsersService service) {
        this.service = service;
    }

    /**
     * Get user by his ID
     *
     * @param id ID of the user
     * @return ResponseEntity with user data or error code
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") final String id) {
        User user = service.getUserById(id);
        if (user == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        // TODO check authorities (ADMIN required)
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(user);
    }
}
