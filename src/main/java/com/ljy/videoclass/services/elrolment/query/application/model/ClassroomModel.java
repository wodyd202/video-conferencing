package com.ljy.videoclass.services.elrolment.query.application.model;

public class ClassroomModel {
    private String classroomCode;
    private String register;

    public ClassroomModel(String classroomCode, String register) {
        this.classroomCode = classroomCode;
        this.register = register;
    }

    public boolean equalsRegister(String register) {
        return this.register.equals(register);
    }
}
