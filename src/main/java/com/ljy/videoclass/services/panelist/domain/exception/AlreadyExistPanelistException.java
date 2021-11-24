package com.ljy.videoclass.services.panelist.domain.exception;

public class AlreadyExistPanelistException extends RuntimeException {
    public AlreadyExistPanelistException(){
        super("이미 해당 아이디를 사용중인 회의자가 존재합니다.");
    }
}
