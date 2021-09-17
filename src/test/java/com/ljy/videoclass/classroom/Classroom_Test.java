package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.classroom.command.application.SimpleOpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.*;
import com.ljy.videoclass.classroom.domain.exception.*;
import com.ljy.videoclass.classroom.domain.read.ClassDateInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassOptionalDateInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    @DisplayName("수업 설명은 필수 여부는 아니지만 공백은 받으면 안됨")
    void emptyDescription(){
        assertThrows(InvalidDescriptionException.class,()->{
            Description.of("");
        });
    }

    @Test
    @DisplayName("수업 설명은 [한글, 숫자, 영문(대,소문자)] 만 허용하며 1자 이상 50자 이하만 허용")
    void invalidDescription(){
        assertThrows(InvalidDescriptionException.class,()->{
            Description.of("#!@#");
        });
    }

    @Test
    @DisplayName("클래스룸 정보 생성")
    void mapClassInfo(){
        ChangeClassInfo classInfo = ChangeClassInfo.builder()
                .title("수업명")
                .color(Color.info)
                .description("수업 설명")
                .build();
        ClassInfoModel classInfoModel = new ClassInfo(classInfo).toModel();
        assertEquals(classInfoModel.getTitle(), "수업명");
        assertEquals(classInfoModel.getDescription(), "수업 설명");
        assertEquals(classInfoModel.getColor(), Color.info);
    }

    @Test
    @DisplayName("수업 시작일은 종료일보다 반드시 작아야함")
    void invalidClassOptionalDateInfo(){
        assertThrows(InvalidClassOptionalDateInfoException.class, ()->{
            ClassOptionalDateInfo.builder()
                    .startDate(LocalDate.of(2020,9,16))
                    .endDate(LocalDate.of(2020,9,15))
                    .autoEnabled(true)
                    .build();
        });
    }

    @Test
    @DisplayName("수업 시작일이 입력되었다면 수업 종료일도 있어야함")
    void emptyEndDate(){
        assertThrows(InvalidClassOptionalDateInfoException.class, ()->{
            ClassOptionalDateInfo.builder()
                    .startDate(LocalDate.of(2020,9,16))
                    .autoEnabled(true)
                    .build();
        });
    }

    @Test
    void validClassOptionalDateInfo(){
        ClassOptionalDateInfo classOptionalDateInfo = ClassOptionalDateInfo.builder()
                .startDate(LocalDate.of(2020, 9, 16))
                .endDate(LocalDate.of(2020, 10, 20))
                .autoEnabled(true)
                .build();
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classOptionalDateInfo.toModel();
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020, 10, 20));
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2020, 9, 16));
        assertTrue(classOptionalDateInfoModel.isAutoEnabled());
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
                        .description("수업 설명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build())
                .classOptionalDateInfo(ChangeClassOptionalDateInfo.builder()
                        .autoEnabled(false)
                        .build())
                .build();
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));
        ClassroomModel classroomModel = classroom.toModel();
        assertNotNull(classroom);
        assertNull(classroomModel.getClassOptionalDateInfo());
        assertEquals(classroomModel.getClassInfo().getDescription(), "수업 설명");
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
    @DisplayName("수업 정보 변경")
    void changeClassInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        classroom.changeClassInfo(ChangeClassInfo.builder()
                        .title("수정 수업명")
                        .description("수정 수업 설명")
                        .color(Color.secondary)
                .build());

        ClassroomModel classroomModel = classroom.toModel();
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
        assertEquals(classroomModel.getClassInfo().getDescription(), "수정 수업 설명");
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
    }

    @Test
    @DisplayName("수업 정보 변경시 수업 설명은 제외해도 됨")
    void changeClassInfo_emptyDescription(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        classroom.changeClassInfo(ChangeClassInfo.builder()
                .title("수정 수업명")
                .color(Color.secondary)
                .build());

        ClassroomModel classroomModel = classroom.toModel();
        assertNull(classroomModel.getClassInfo().getDescription());
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
                    .color(Color.secondary)
                    .build());
        });
    }

    @Test
    @DisplayName("수업 날짜 변경")
    void changeClassOptionalDateInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        ChangeClassOptionalDateInfo classOptionalDateInfo = ChangeClassOptionalDateInfo.builder()
                .startDate(LocalDate.of(2019, 10,10))
                .endDate(LocalDate.of(2020, 10,10))
                .autoEnabled(false)
                .build();

        classroom.changeClassOptionalDateInfo(classOptionalDateInfo);
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroom.toModel().getClassOptionalDateInfo();
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020,10,10));
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2019,10,10));
        assertFalse(classOptionalDateInfoModel.isAutoEnabled());
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
