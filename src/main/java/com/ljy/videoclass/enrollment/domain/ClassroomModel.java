package com.ljy.videoclass.enrollment.domain;

public class ClassroomModel {
    private String code;
    private String register;

    public ClassroomModel(String code, String register) {
        this.code = code;
        this.register = register;
    }

    public String getCode() {
        return code;
    }

    public String getRegister() {
        return register;
    }
}
