package it.sevenbits.backend.taskmanager.web.service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class that handles exceptions from service
 */
@ControllerAdvice
public class TaskServiceExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle exception about a non-integer value of page/size
     *
     * @param ex exception of number format
     * @return ResponseEntity with message
     */
    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(final NumberFormatException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }
}
