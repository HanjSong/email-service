package com.siteminder.webmail.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmailControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(EmailControllerAdvice.class);

    /**
     * Handle exception
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleException(RuntimeException ex) {
        logger.error("Exception caught by handler advice", ex);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
