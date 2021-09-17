package com.ljy.videoclass.classroom.domain.exception;

public class InvalidDescriptionException extends IllegalArgumentException{
    public InvalidDescriptionException(String msg){
        super(msg);
    }
}
