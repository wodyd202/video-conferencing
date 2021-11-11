package com.ljy.videoclass.services.user.domain.exception;

public class InvalidImageException extends IllegalArgumentException{
    public InvalidImageException(String msg){
        super(msg);
    }
}
