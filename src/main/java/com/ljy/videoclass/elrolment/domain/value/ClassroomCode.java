package com.ljy.videoclass.elrolment.domain.value;

public class ClassroomCode {
    private final String code;

    private ClassroomCode(String code) {
        this.code = code;
    }

    public static ClassroomCode of(String code){
        return new ClassroomCode(code);
    }

    public String get() {
        return code;
    }
}
