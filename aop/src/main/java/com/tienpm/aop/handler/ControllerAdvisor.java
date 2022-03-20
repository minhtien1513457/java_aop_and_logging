package com.tienpm.aop.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        AtomicReference<String> message = new AtomicReference<>("");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.set(fieldName + ": " + errorMessage + ";");
        });
        ExceptionBody body = ExceptionBody.of(System.currentTimeMillis(), message.toString());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HandledException.class})
    public ResponseEntity<Object> handleHandledExceptionV2(
            HandledException ex) {
        ExceptionBody body = ExceptionBody.of(System.currentTimeMillis(), ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex
            ) {
        ExceptionBody body = ExceptionBody.of(System.currentTimeMillis(), ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleDefaultException(Exception ex) {
        ExceptionBody body = ExceptionBody.of(System.currentTimeMillis(), "Internal server error");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
