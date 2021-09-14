package com.ljy.videoclass.classroom.domain.exception;

public class AlreadyActiveClassException extends IllegalArgumentException{
    public AlreadyActiveClassException(String msg){
        super(msg);
    }
}
