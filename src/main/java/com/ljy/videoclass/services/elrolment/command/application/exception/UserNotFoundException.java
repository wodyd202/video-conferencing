package com.ljy.videoclass.services.elrolment.command.application.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("해당 사용자 정보가 존재하지 않습니다.");
    }
}
