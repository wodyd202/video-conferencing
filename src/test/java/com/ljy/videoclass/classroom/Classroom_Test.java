package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.classroom.command.application.SimpleOpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.*;
import com.ljy.videoclass.classroom.domain.exception.*;
import com.ljy.videoclass.classroom.domain.read.ClassDateInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassDateInfo;
import com.ljy.videoclass.classroom.domain.value.Color;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.value.Title;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.util.Arrays;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Classroom_Test {

    @Test
    @DisplayName("수업명은 반드시 입력해야함")
    void emptyTitle(){
        assertThrows(InvalidClassTitleException.class, ()->{
            Title.of("");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "수업명%",
            " ",
            "   "
    })
    @DisplayName("수업명은 [한글, 숫자, 영어(대,소문자)] 만 허용하며 1자 이상 20자 이하만 허용")
    void invalidTitle(String title){
        assertThrows(InvalidClassTitleException.class, ()->{
           Title.of(title);
        });
    }

    @Test
    void validTitle(){
        Title title = Title.of("수업명");
        assertEquals(title, Title.of("수업명"));
        assertEquals(title.get(),"수업명");
    }

    @Test
    @DisplayName("수업 요일은 하나 이상 입력해야함")
    void emptyDayOfWeeks(){
        assertThrows(InvalidClassDateInfoException.class, ()->{
            new ClassDateInfo(ChangeClassDateInfo.builder()
                            .endHour(16)
                            .startHour(14)
                            .build());
        });
    }

    @Test
    @DisplayName("수업 시작 시간은 종료 시간보다 작아야함")
    void invalidClassDateInfo(){
        assertThrows(InvalidClassDateInfoException.class, ()->{
            new ClassDateInfo(ChangeClassDateInfo.builder()
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .endHour(14)
                    .startHour(17)
                    .build());
        });
    }

    @Test
    void validClassDateInfo(){
        ClassDateInfo classDateInfo = new ClassDateInfo(ChangeClassDateInfo.builder()
                                        .dayOfWeek(DayOfWeek.MONDAY)
                                        .endHour(16)
                                        .startHour(14)
                                        .build());
        assertNotNull(classDateInfo.toModel());
    }

    @Test
    void create(){
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));
        assertNotNull(classroom);
    }

    @Test
    @Disabled
    @DisplayName("겹치는 수업이 존재하면 안됨")
    void open_alreadyExistThatTimeClassroom(){
        OpenClassroom openClassroom = aOpenClassroom()
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(0)
                        .endHour(3)
                        .build())
                .build();
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));

        ClassroomRepository classroomRepository = mock(ClassroomRepository.class);
        OpenClassroomValidator openClassroomValidator = new SimpleOpenClassroomValidator(classroomRepository);

        OpenClassroom openClassroom_2 = aOpenClassroom()
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(1)
                        .endHour(5)
                        .build())
                .build();
        Classroom classroom_2 = Classroom.createWith(openClassroom_2, Register.of("00000000"));
        when(classroomRepository.findClassDateInfoByRegister(Register.of("00000000")))
                .thenReturn(Arrays.asList(classroom_2));

        assertThrows(ClassTimeOverlapException.class, ()->{
            classroom.open(openClassroomValidator);
        });
    }

    @Test
    @DisplayName("수업 비활성화")
    void disable() {
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();
        assertTrue(classroom.isDisable());
    }

    @Test
    @DisplayName("수업이 이미 비활성화 되어있으면 안됨")
    void disable_alreadyEnabled(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();
        assertThrows(AlreadyDisabledClassException.class,()->{
            classroom.disable();
        });
    }

    @Test
    @DisplayName("수업 활성화")
    void active(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        classroom.active();
        assertFalse(classroom.isDisable());
    }

    @Test
    @DisplayName("이미 활성화된 수업을 다시 활성화 할 수 없음")
    void active_alreadyActive(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        assertThrows(AlreadyActiveClassException.class,()->{
            classroom.active();
        });
    }

    @Test
    @DisplayName("수업명 및 색상 변경")
    void changeClassInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        classroom.changeClassInfo(ChangeClassInfo.builder()
                        .title("수정 수업명")
                        .color(Color.SECONDARY)
                .build());
        ClassroomModel classroomModel = classroom.toModel();
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
        assertEquals(classroomModel.getClassInfo().getColor(), Color.SECONDARY);
    }

    @Test
    @DisplayName("비활성화된 수업은 수업정보를 변경할 수 없음")
    void changeClassInfo_alreadyDisabled(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        assertThrows(AlreadyDisabledClassException.class,()->{
            classroom.changeClassInfo(ChangeClassInfo.builder()
                    .title("수정 수업명")
                    .color(Color.SECONDARY)
                    .build());
        });
    }

    @Test
    @DisplayName("수업 시간 변경")
    void changeClassDateInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        classroom.changeClassDateInfo(ChangeClassDateInfo.builder()
                        .startHour(1)
                        .endHour(6)
                        .dayOfWeek(DayOfWeek.FRIDAY)
                .build());

        ClassDateInfoModel classDateInfo = classroom.toModel().getClassDateInfo();
        assertEquals(classDateInfo.getDayOfWeek(), DayOfWeek.FRIDAY);
        assertEquals(classDateInfo.getEndHour(),6);
        assertEquals(classDateInfo.getStartHour(),1);
    }

    @Test
    @DisplayName("비활성화된 수업은 수업시간을 변경할 수 없음")
    void changeClassDateInfo_alreadyDisabled(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        assertThrows(AlreadyDisabledClassException.class,()->{
        classroom.changeClassDateInfo(ChangeClassDateInfo.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startHour(1)
                .endHour(6)
                .build());
        });
    }

    private Classroom openClassroom(OpenClassroom openClassroom){
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));
        classroom.open(mock(OpenClassroomValidator.class));
        return classroom;
    }

}
