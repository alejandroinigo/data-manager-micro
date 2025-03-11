package com.developer.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger exceptionLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        exceptionLogger.error("Illegal argument exception", ex);
        final Throwable rootCause = ExceptionUtils.getRootCause(ex);
        final String body = rootCause == null ? ex.getMessage() : rootCause.getMessage();
        return new ResponseEntity<>(body , new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
