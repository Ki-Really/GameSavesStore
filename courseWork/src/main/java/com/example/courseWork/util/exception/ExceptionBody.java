package com.example.courseWork.util.exception;

import java.util.Map;

public class ExceptionBody {
    private String message;
    private Map<String,String> errors;

    public ExceptionBody(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public ExceptionBody(String message) {
        this.message = message;
    }

    public ExceptionBody() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
