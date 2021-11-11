package com.ljy.videoclass.services.user.domain.exception;

public class InvalidNameException extends IllegalArgumentException{
    public InvalidNameException(String msg){
        super(msg);
    }
}
