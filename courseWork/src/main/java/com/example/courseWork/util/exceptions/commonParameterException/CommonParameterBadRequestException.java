package com.example.courseWork.util.exceptions.commonParameterException;

import java.util.List;

public class CommonParameterBadRequestException extends RuntimeException {
    private List<String> errors;

    public CommonParameterBadRequestException(String message, List<String>errors){
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
