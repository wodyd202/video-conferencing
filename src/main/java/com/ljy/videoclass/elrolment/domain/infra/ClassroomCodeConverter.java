package com.ljy.videoclass.elrolment.domain.infra;

import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;

import javax.persistence.AttributeConverter;

public class ClassroomCodeConverter implements AttributeConverter<ClassroomCode, String> {
    @Override
    public String convertToDatabaseColumn(ClassroomCode classroomCode) {
        return classroomCode.get();
    }

    @Override
    public ClassroomCode convertToEntityAttribute(String s) {
        return ClassroomCode.of(s);
    }
}
