package com.ljy.videoclass.services.classroom;

import com.ljy.videoclass.services.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
abstract public class ClassroomTest {
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private OpenClassroomService openClassroomService;

    protected Classroom findClassroom(String code){
        return classroomRepository.findByCode(ClassroomCode.of(code)).get();
    }

    protected ClassroomModel newClassroom(OpenClassroom openClassroom, Register register){
        return openClassroomService.open(openClassroom, register);
    }
}
