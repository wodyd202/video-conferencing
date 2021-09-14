package com.ljy.videoclass.classroom;

public class InvalidClassTitleException extends IllegalArgumentException{
    public InvalidClassTitleException(String msg){
        super(msg);
    }
}
