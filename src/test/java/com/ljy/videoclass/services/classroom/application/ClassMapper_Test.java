package com.ljy.videoclass.services.classroom.application;

import com.ljy.videoclass.services.classroom.command.application.ClassroomMapper;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassDateInfoModel;
import com.ljy.videoclass.services.classroom.domain.read.ClassInfoModel;
import com.ljy.videoclass.services.classroom.domain.read.ClassOptionalDateInfoModel;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;

import static com.ljy.videoclass.services.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.*;

/**
 * mapper 테스트
 */
public class ClassMapper_Test {
    ClassroomMapper classroomMapper = new ClassroomMapper();

    @Test
    void mapFrom(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ChangeClassInfo originClassroomInfo = openClassroom.getClassInfo();
        ChangeClassDateInfo originClassDateInfo = openClassroom.getClassDateInfo();
        ChangeClassOptionalDateInfo originClassOptionalDateInfo = openClassroom.getClassOptionalDateInfo();
        Register register = Register.of("register");

        // when
        ClassroomModel classroomModel = classroomMapper.mapFrom(openClassroom, register).toModel();

        // then
        assertNotNull(classroomModel.getCode());

        ClassInfoModel classInfo = classroomModel.getClassInfo();
        ClassDateInfoModel classDateInfo = classroomModel.getClassDateInfo();
        ClassOptionalDateInfoModel optionalDateInfoModel = classroomModel.getClassOptionalDateInfo();

        assertEquals(classInfo.getDescription(), originClassroomInfo.getDescription());
        assertEquals(classInfo.getColor(), originClassroomInfo.getColor());
        assertEquals(classInfo.getTitle(), originClassroomInfo.getTitle());

        assertEquals(classDateInfo.getDayOfWeek(), originClassDateInfo.getDayOfWeek());
        assertEquals(classDateInfo.getStartHour(), originClassDateInfo.getStartHour());
        assertEquals(classDateInfo.getEndHour(), originClassDateInfo.getEndHour());

        if(originClassOptionalDateInfo != null){
            assertEquals(optionalDateInfoModel.getEndDate(), originClassOptionalDateInfo.getEndDate());
            assertEquals(optionalDateInfoModel.getStartDate(), originClassOptionalDateInfo.getStartDate());
        }else{
            assertNull(optionalDateInfoModel.getEndDate());
            assertNull(optionalDateInfoModel.getStartDate());
            assertNull(optionalDateInfoModel.isAutoEnabled());
        }

        assertEquals(classroomModel.getRegister(), "register");
    }
}
