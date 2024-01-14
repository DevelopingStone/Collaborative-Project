package com.zerobase.oriticket.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<Void> handleCustomException(AbstractException e) {
        log.error("CustomException..........");
        log.error(e.getMessage());

        return ResponseEntity.status(e.getErrorCode()).build();
    }

    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<Void> handleTypeMismatchException(TypeMismatchException e) {
        log.error("TypeMismatchException..........");
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Void> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException..........");
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException..........");
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

}
