package com.ljy.videoclass.user.command.domain.exception;

public class InvalidUserIdException extends IllegalArgumentException {
    public InvalidUserIdException(String msg){
        super(msg);
    }
}