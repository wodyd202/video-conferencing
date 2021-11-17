package com.ljy.videoclass.services.elrolment.command.application.exception;

public class ElrolmentNotFoundException extends RuntimeException {
    public ElrolmentNotFoundException(){
        super("해당 수강신청 정보가 존재하지 않습니다.");
    }
}
