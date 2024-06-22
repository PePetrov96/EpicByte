package com.project.EpicByte.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> handleInvalidUUIDFormat(MethodArgumentTypeMismatchException ex) {
        // handle exception here, for instance log it or send notification; return custom message or object
        return ResponseEntity.ok(null);
    }
}
