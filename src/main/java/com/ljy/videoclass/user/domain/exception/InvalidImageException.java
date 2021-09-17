package com.ljy.videoclass.user.domain.exception;

public class InvalidImageException extends IllegalArgumentException{
    public InvalidImageException(String msg){
        super(msg);
    }
}
