package com.ljy.videoclass.services.classroom.application;

import com.ljy.videoclass.services.classroom.ClassroomFixture;
import com.ljy.videoclass.services.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.services.classroom.command.application.ClassroomServiceHelper;
import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ClassroomServiceHelper_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired ClassroomRepository classroomRepository;

    @Test
    void notFound(){
        assertThrows(ClassroomNotFoundException.class,()->{
            ClassroomServiceHelper.findByCodeAndRegister(classroomRepository, ClassroomCode.of("notfound"), Register.of("00000000"));
        });
    }

    @Test
    void found(){
        ClassroomModel classroomModel = openClassroomService.open(ClassroomFixture.aOpenClassroom().build(), Register.of("00000000"));
        assertDoesNotThrow(()->{
            ClassroomServiceHelper.findByCodeAndRegister(classroomRepository, ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));
        });
    }

}
