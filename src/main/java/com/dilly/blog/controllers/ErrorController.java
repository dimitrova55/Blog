package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


// @ControllerAdvice annotation enables global exception handling across all our controllers.

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    /** Handles any Exception **/

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception){

        log.error("Caught exception", exception);

        // the FieldError class is not used here!
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error.")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /** Handles Illegal Argument Exceptions **/

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException (
            IllegalArgumentException exception)
    {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /** Handles Illegal State Exceptions **/

    @ExceptionHandler(exception = IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException( IllegalStateException exception){

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    /** Handles Authentication Errors **/

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException exception){

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Incorrect username or password.")
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /** Handles non-existing entity in the database**/

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException (EntityNotFoundException exception) {
         ApiErrorResponse error = ApiErrorResponse.builder()
                 .status(HttpStatus.NOT_FOUND.value())
                 .message(exception.getMessage())
                 .build();

         return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
