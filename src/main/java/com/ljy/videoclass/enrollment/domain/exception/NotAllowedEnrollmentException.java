package com.ljy.videoclass.enrollment.domain.exception;

public class NotAllowedEnrollmentException extends IllegalArgumentException {
    public NotAllowedEnrollmentException(String msg) {
        super(msg);
    }
}
