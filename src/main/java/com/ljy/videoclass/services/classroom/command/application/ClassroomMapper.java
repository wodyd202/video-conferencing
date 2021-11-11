package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.*;
import com.ljy.videoclass.services.classroom.domain.value.*;
import org.springframework.stereotype.Component;

@Component
public class ClassroomMapper {

    public Classroom mapFrom(OpenClassroom openClassroom, Register register) {
        ChangeClassInfo classInfo = openClassroom.getClassInfo();
        ChangeClassDateInfo classDateInfo = openClassroom.getClassDateInfo();
        ChangeClassOptionalDateInfo classOptionalDateInfo = openClassroom.getClassOptionalDateInfo();
        return Classroom.builder()
                .classInfo(mapFrom(classInfo))
                .classDateInfo(mapFrom(classDateInfo))
                .classOptionalDateInfo(mapFrom(classOptionalDateInfo))
                .register(register)
                .build();
    }

    public ClassInfo mapFrom(ChangeClassInfo changeClassInfo){
        return ClassInfo.builder()
                .description(changeClassInfo.getDescription() == null ? null : Description.of(changeClassInfo.getDescription()))
                .color(changeClassInfo.getColor())
                .title(Title.of(changeClassInfo.getTitle()))
                .build();
    }

    public ClassDateInfo mapFrom(ChangeClassDateInfo changeClassDateInfo){
        return ClassDateInfo.builder()
                .dayOfWeek(changeClassDateInfo.getDayOfWeek())
                .startHour(changeClassDateInfo.getStartHour())
                .endHour(changeClassDateInfo.getEndHour())
                .build();
    }

    public ClassOptionalDateInfo mapFrom(ChangeClassOptionalDateInfo changeClassOptionalDateInfo){
        if(changeClassOptionalDateInfo == null) {
            return ClassOptionalDateInfo.getInstance();
        }
        return ClassOptionalDateInfo.builder()
                .autoEnabled(changeClassOptionalDateInfo.getAutoEnabled())
                .startDate(changeClassOptionalDateInfo.getStartDate())
                .endDate(changeClassOptionalDateInfo.getEndDate())
                .build();
    }

}
