package com.example.courseWork.util.exceptions.personException;

public class PasswordsNotMatchException extends RuntimeException{
    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
