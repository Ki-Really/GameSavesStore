package com.example.courseWork.controllerAdvice;

import com.example.courseWork.util.ErrorResponse;
import com.example.courseWork.util.exceptions.gameException.GameBadRequestException;
import com.example.courseWork.util.exceptions.gameException.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameControllerAdvice {
    @ExceptionHandler(GameBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ErrorResponse> handleGameCreationFailedException(
            GameBadRequestException e){
        ErrorResponse response = new ErrorResponse(e.getMessage(),e.getErrors());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleGameNotFoundException(
            GameNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
