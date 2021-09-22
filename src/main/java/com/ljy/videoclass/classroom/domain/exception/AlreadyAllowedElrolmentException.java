package com.ljy.videoclass.classroom.domain.exception;

public class AlreadyAllowedElrolmentException extends IllegalArgumentException {
    public AlreadyAllowedElrolmentException(String msg) {
        super(msg);
    }
}
