package com.ljy.videoclass.classroom.domain.exception;

public class InvalidClassTitleException extends IllegalArgumentException{
    public InvalidClassTitleException(String msg){
        super(msg);
    }
}
