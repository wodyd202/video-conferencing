package com.ljy.videoclass.services.classroom;

import com.ljy.videoclass.services.classroom.command.application.ClassroomMapper;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.value.Register;

import java.time.DayOfWeek;

public class ClassroomFixture {
    private static ClassroomMapper classroomMapper = new ClassroomMapper();

    public static OpenClassroom.OpenClassroomBuilder aOpenClassroom() {
        return OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build());
    }

    public static Classroom aClassroom(Register register) {
        OpenClassroom openClassroom = aOpenClassroom().build();
        return classroomMapper.mapFrom(openClassroom, register);
    }
}
