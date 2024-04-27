package com.example.courseWork.util.exceptions.graphicCommonException;

import java.util.List;

public class GraphicCommonBadRequestException extends RuntimeException {
    private List<String> errors;

    public GraphicCommonBadRequestException(String message, List<String>errors){
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
