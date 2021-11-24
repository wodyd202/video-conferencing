package com.ljy.videoclass.services.conference.command.application.exception;

public class PanelistNotFoundException extends RuntimeException {
    public PanelistNotFoundException(){
        super("회의자 정보가 존재하지 않습니다.");
    }
}
