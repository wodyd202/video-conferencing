package com.ljy.videoclass.user.domain.exception;

public class InvalidEmailException extends IllegalArgumentException{
    public InvalidEmailException(String msg){
        super(msg);
    }
}
