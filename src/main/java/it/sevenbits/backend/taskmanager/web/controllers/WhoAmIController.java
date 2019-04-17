package it.sevenbits.backend.taskmanager.web.controllers;

import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.web.service.whoami.WhoAmIService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller that handling requests for current user data
 */
@Controller
@RequestMapping(value = "/whoami")
public class WhoAmIController {
    private final WhoAmIService whoAmIService;

    /**
     * Create controller
     *
     * @param whoAmIService service that works with token of current user and can get user data from database
     */
    public WhoAmIController(final WhoAmIService whoAmIService) {
        this.whoAmIService = whoAmIService;
    }

    /**
     * Get information about current user
     *
     * @param request request for user info
     * @return user info
     * 200 - found user info in database
     * 403 - user does not have access to the resource
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<User> whoAmI(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(whoAmIService.getCurrentUserInfo(token));
    }
}
