package com.example.courseWork.controllerAdvice;

import com.example.courseWork.util.*;
import com.example.courseWork.util.exception.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ExceptionBody handlePersonNotFoundException(PersonNotFoundException e){
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(PersonNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ExceptionBody handlePersonNotCreatedException(PersonNotCreatedException e){
        return new ExceptionBody(e.getMessage());
    }
    @ExceptionHandler(PersonAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<PersonErrorResponse> handlePersonAlreadyExistsException(PersonAlreadyExistsException ex) {
        PersonErrorResponse response = new PersonErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<PersonErrorResponse> handleLoginFailedException(LoginFailedException ex) {
        PersonErrorResponse response = new PersonErrorResponse(ex.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
