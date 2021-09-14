package com.ljy.videoclass.classroom.domain.exception;

public class AlreadyDisabledClassException extends IllegalArgumentException {
    public AlreadyDisabledClassException(String msg) {
        super(msg);
    }
}
