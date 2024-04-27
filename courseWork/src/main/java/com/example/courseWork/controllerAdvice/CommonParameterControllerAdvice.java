package com.example.courseWork.controllerAdvice;

import com.example.courseWork.util.ErrorResponse;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterBadRequestException;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterDeleteException;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonParameterControllerAdvice {
    @ExceptionHandler(CommonParameterBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ErrorResponse> handleCommonParameterCreationFailedException(
            CommonParameterBadRequestException e){
        ErrorResponse response = new ErrorResponse(e.getMessage(),e.getErrors());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonParameterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleCommonParameterNotFoundException(
            CommonParameterNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommonParameterDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleCommonParameterDeleteException(
            CommonParameterDeleteException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
