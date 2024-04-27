package com.example.courseWork.util.exceptions.gameStateException;

import java.util.List;

public class GameStateBadRequestException extends RuntimeException {
    private List<String> errors;

    public GameStateBadRequestException(String message, List<String>errors){
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
