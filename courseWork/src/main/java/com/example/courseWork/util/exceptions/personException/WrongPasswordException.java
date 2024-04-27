package com.example.courseWork.util.exceptions.personException;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String message){
        super(message);
    }
}
