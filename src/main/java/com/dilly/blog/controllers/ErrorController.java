package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


// @ControllerAdvice annotation enables global exception handling across all our controllers.

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

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

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException (IllegalArgumentException exception){

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
