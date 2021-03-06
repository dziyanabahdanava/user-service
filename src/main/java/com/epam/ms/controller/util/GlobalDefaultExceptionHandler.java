package com.epam.ms.controller.util;

import com.epam.ms.service.exception.ConflictingDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorData> constraintViolationErrorHandler(ConstraintViolationException e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        log.error("Validation for the bean failed", e);
        return ResponseEntity.badRequest().body(errorData);
    }

    @ExceptionHandler(value = org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ErrorData> hibernateConstraintViolationErrorHandler(org.hibernate.exception.ConstraintViolationException e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        log.error("Could not execute statement", e);
        return ResponseEntity.badRequest().body(errorData);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<Void> emptyResultDataAccessErrorHandler(EmptyResultDataAccessException e) {
        log.error("Resource not found", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorData> baseErrorHandler(Exception e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        log.error("Unexpected error", e);
        return ResponseEntity.status(500).body(errorData);
    }

    @ExceptionHandler(value = ConflictingDataException.class)
    public ResponseEntity<ErrorData> conflictingDataErrorHandler(ConflictingDataException e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        log.error("The request could not be completed due to a conflict with the current state of the target resource", e);
        return ResponseEntity.status(409).body(errorData);
    }
}
