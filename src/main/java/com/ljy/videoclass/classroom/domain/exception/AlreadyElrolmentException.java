package com.ljy.videoclass.classroom.domain.exception;

public class AlreadyElrolmentException extends IllegalArgumentException{
    public AlreadyElrolmentException(){
        super("이미 수강 신청한 수업입니다.");
    }
}
