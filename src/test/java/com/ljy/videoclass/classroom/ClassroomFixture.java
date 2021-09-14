package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;

import java.time.DayOfWeek;

public class ClassroomFixture {

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
}
