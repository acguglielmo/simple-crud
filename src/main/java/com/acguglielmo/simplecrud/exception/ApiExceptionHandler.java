package com.acguglielmo.simplecrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<String> handleException(RuntimeException ex, WebRequest request) {

    	return ResponseEntity
    		.status(HttpStatus.INTERNAL_SERVER_ERROR)
    		.body( ex.getMessage() );

    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<String> handle(NotFoundException ex) {

    	return ResponseEntity
    		.status(HttpStatus.BAD_REQUEST)
    		.body( ex.getMessage() );

    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    protected ResponseEntity<String> handle(EntityAlreadyExistsException ex) {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body( ex.getMessage() );

    }

}
