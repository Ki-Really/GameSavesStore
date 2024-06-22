package com.example.courseWork.controllerAdvice;

import com.example.courseWork.util.ErrorResponse;
import com.example.courseWork.util.exceptions.gameStateParameterException.GameStateParameterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameStateParameterControllerAdvice {
    @ExceptionHandler(GameStateParameterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleGameStateParameterNotFoundException(GameStateParameterNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
