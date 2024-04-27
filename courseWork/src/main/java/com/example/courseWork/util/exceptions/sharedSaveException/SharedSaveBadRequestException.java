package com.example.courseWork.util.exceptions.sharedSaveException;

import java.util.List;

public class SharedSaveBadRequestException extends RuntimeException {
    private List<String> errors;

    public SharedSaveBadRequestException(String message, List<String>errors){
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
