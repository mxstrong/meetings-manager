package com.example.meetingsmanager.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(ConflictingScheduleException.class)
    public ResponseEntity<Error> handleException(ConflictingScheduleException e) {
        Error error = new Error(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(error, error.httpStatus());
    }
}
