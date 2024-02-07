package com.example.courseWork.util;

public class PersonErrorResponse {
    private String message;
    private long millis;

    public PersonErrorResponse(String message, long millis) {
        this.message = message;
        this.millis = millis;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
