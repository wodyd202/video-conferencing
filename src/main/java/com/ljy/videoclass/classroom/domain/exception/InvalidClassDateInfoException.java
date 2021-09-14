package com.ljy.videoclass.classroom.domain.exception;

public class InvalidClassDateInfoException extends IllegalArgumentException{
    public InvalidClassDateInfoException(String msg){
        super(msg);
    }
}
