package com.acguglielmo.simplecrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.acguglielmo.simplecrud.response.ErrorResponse;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {

    	return ResponseEntity
    		.status(HttpStatus.INTERNAL_SERVER_ERROR)
    		.body( ErrorResponse.builder().message("Internal server error").build()  );

    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(NotFoundException ex) {

    	return ResponseEntity
    		.status(HttpStatus.BAD_REQUEST)
    		.body( ErrorResponse.builder().message( ex.getMessage() ).build()  );

    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    protected ResponseEntity<ErrorResponse> handle(EntityAlreadyExistsException ex) {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body( ErrorResponse.builder().message( ex.getMessage() ).build()  );

    }

}
