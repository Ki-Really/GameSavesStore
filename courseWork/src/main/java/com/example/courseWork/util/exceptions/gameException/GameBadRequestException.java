package com.example.courseWork.util.exceptions.gameException;

import java.util.List;

public class GameBadRequestException extends RuntimeException{
    private List<String> errors;

    public GameBadRequestException(String message, List<String>errors){
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
