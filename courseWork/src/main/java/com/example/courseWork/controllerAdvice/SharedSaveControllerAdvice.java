package com.example.courseWork.controllerAdvice;

import com.example.courseWork.util.ErrorResponse;
import com.example.courseWork.util.exceptions.sharedSaveException.SharedSaveBadRequestException;
import com.example.courseWork.util.exceptions.sharedSaveException.SharedSaveNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SharedSaveControllerAdvice {
    @ExceptionHandler(SharedSaveBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ErrorResponse> handleSharedSaveBadRequestException(
            SharedSaveBadRequestException e){
        ErrorResponse response = new ErrorResponse(e.getMessage(),e.getErrors());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SharedSaveNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleSharedSaveNotFoundException(
            SharedSaveNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
