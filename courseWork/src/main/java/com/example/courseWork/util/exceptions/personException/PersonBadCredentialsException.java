package com.example.courseWork.util.exceptions.personException;

import java.util.List;

public class PersonBadCredentialsException extends RuntimeException{
    private List<String> errors;
    public PersonBadCredentialsException(String message, List<String>errors){
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
