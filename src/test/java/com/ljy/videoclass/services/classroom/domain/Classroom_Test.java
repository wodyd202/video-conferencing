package com.ljy.videoclass.services.classroom.domain;

import com.ljy.videoclass.services.classroom.command.application.ClassroomMapper;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.*;
import com.ljy.videoclass.services.classroom.domain.value.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.ljy.videoclass.services.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class Classroom_Test {

    private ClassroomMapper classroomMapper = new ClassroomMapper();

    @Test
    @DisplayName("수업명은 반드시 입력해야함")
    void emptyTitle(){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
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
        // when
        assertThrows(IllegalArgumentException.class, ()->{
           Title.of(title);
        });
    }

    @Test
    void validTitle(){
        // given
        Title title = Title.of("수업명");

        // when
        assertEquals(title, Title.of("수업명"));
        assertEquals(title.get(),"수업명");
    }

    @Test
    @DisplayName("수업 설명은 필수 여부는 아니지만 공백은 받으면 안됨")
    void emptyDescription(){
        // when
        assertThrows(IllegalArgumentException.class,()->{
            Description.of("");
        });
    }

    @Test
    @DisplayName("수업 설명은 [한글, 숫자, 영문(대,소문자)] 만 허용하며 1자 이상 50자 이하만 허용")
    void invalidDescription(){
        // when
        assertThrows(IllegalArgumentException.class,()->{
            Description.of("#!@#");
        });
    }

    @Test
    @DisplayName("클래스룸 정보 생성")
    void mapClassInfo(){
        // given
        ChangeClassInfo classInfo = ChangeClassInfo.builder()
                .title("수업명")
                .color(Color.info)
                .description("수업 설명")
                .build();

        // when
        ClassInfoModel classInfoModel = classroomMapper.mapFrom(classInfo).toModel();

        // then
        assertEquals(classInfoModel.getTitle(), "수업명");
        assertEquals(classInfoModel.getDescription(), "수업 설명");
        assertEquals(classInfoModel.getColor(), Color.info);
    }

    @Test
    @DisplayName("수업 시작일은 종료일보다 반드시 작아야함")
    void invalidClassOptionalDateInfo(){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
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
        // when
        assertThrows(IllegalArgumentException.class, ()->{
            ClassOptionalDateInfo.builder()
                    .startDate(LocalDate.of(2020,9,16))
                    .autoEnabled(true)
                    .build();
        });
    }

    @Test
    void validClassOptionalDateInfo(){
        // given
        ClassOptionalDateInfo classOptionalDateInfo = ClassOptionalDateInfo.builder()
                .startDate(LocalDate.of(2020, 9, 16))
                .endDate(LocalDate.of(2020, 10, 20))
                .autoEnabled(true)
                .build();

        // when
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classOptionalDateInfo.toModel();

        // then
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020, 10, 20));
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2020, 9, 16));
        assertTrue(classOptionalDateInfoModel.isAutoEnabled());
    }

    @Test
    @DisplayName("수업 요일은 하나 이상 입력해야함")
    void emptyDayOfWeeks(){
        // given
        ChangeClassDateInfo changeClassDateInfo = ChangeClassDateInfo.builder()
                .endHour(16)
                .startHour(14)
                .build();

        // when
        assertThrows(IllegalArgumentException.class, ()->{
            classroomMapper.mapFrom(changeClassDateInfo);
        });
    }

    @Test
    @DisplayName("수업 시작 시간은 종료 시간보다 작아야함")
    void invalidClassDateInfo(){
        // given
        ChangeClassDateInfo changeClassDateInfo = ChangeClassDateInfo.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .endHour(14)
                .startHour(17)
                .build();

        // when
        assertThrows(IllegalArgumentException.class, ()->{
            classroomMapper.mapFrom(changeClassDateInfo);
        });
    }

    @Test
    void validClassDateInfo(){
        // given
        ChangeClassDateInfo changeClassDateInfo = ChangeClassDateInfo.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .endHour(16)
                .startHour(14)
                .build();
        ClassDateInfo classDateInfo = classroomMapper.mapFrom(changeClassDateInfo);

        // when
        assertNotNull(classDateInfo.toModel());
    }

    @Test
    void create(){
        // given
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
                .build();

        // when
        Classroom classroom = classroomMapper.mapFrom(openClassroom, Register.of("00000000"));
        ClassroomModel classroomModel = classroom.toModel();

        // then
        assertNotNull(classroom);
        assertNull(classroomModel.getClassOptionalDateInfo().getStartDate());
        assertNull(classroomModel.getClassOptionalDateInfo().getEndDate());
        assertNull(classroomModel.getClassOptionalDateInfo().isAutoEnabled());
        assertEquals(classroomModel.getClassInfo().getDescription(), "수업 설명");
    }

    @Test
    @DisplayName("수업 비활성화")
    void disable() {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        assertDoesNotThrow(()->{
            classroom.disable();
        });
    }

    @Test
    @DisplayName("수업이 이미 비활성화 되어있으면 안됨")
    void disable_alreadyEnabled(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        classroom.disable();

        // then
        assertThrows(IllegalArgumentException.class,()->{
            classroom.disable();
        });
    }

    @Test
    @DisplayName("수업 활성화")
    void active(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        // when
        assertDoesNotThrow(()->{
            classroom.active();
        });
    }

    @Test
    @DisplayName("이미 활성화된 수업을 다시 활성화 할 수 없음")
    void active_alreadyActive(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        assertThrows(IllegalArgumentException.class,()->{
            classroom.active();
        });
    }

    @Test
    @DisplayName("수업 정보 변경")
    void changeClassInfo(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        ChangeClassInfo changeClassInfo = ChangeClassInfo.builder()
                .title("수정 수업명")
                .description("수정 수업 설명")
                .color(Color.secondary)
                .build();
        ClassInfo classInfo = classroomMapper.mapFrom(changeClassInfo);
        classroom.changeClassInfo(classInfo);
        ClassroomModel classroomModel = classroom.toModel();

        // then
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
        assertEquals(classroomModel.getClassInfo().getDescription(), "수정 수업 설명");
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
    }

    @Test
    @DisplayName("수업 정보 변경시 수업 설명은 제외해도 됨")
    void changeClassInfo_emptyDescription(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        ChangeClassInfo changeClassInfo = ChangeClassInfo.builder()
                .title("수정 수업명")
                .color(Color.secondary)
                .build();
        ClassInfo classInfo = classroomMapper.mapFrom(changeClassInfo);
        classroom.changeClassInfo(classInfo);
        ClassroomModel classroomModel = classroom.toModel();

        // then
        assertNull(classroomModel.getClassInfo().getDescription());
    }

    @Test
    @DisplayName("비활성화된 수업은 수업정보를 변경할 수 없음")
    void changeClassInfo_alreadyDisabled(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        // when
        assertThrows(IllegalArgumentException.class,()->{
            ChangeClassInfo changeClassInfo = ChangeClassInfo.builder()
                    .title("수정 수업명")
                    .color(Color.secondary)
                    .build();
            ClassInfo classInfo = classroomMapper.mapFrom(changeClassInfo);
            classroom.changeClassInfo(classInfo);
        });
    }

    @Test
    @DisplayName("수업 날짜 변경")
    void changeClassOptionalDateInfo(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        ChangeClassOptionalDateInfo changeClassOptionalDateInfo = ChangeClassOptionalDateInfo.builder()
                .startDate(LocalDate.of(2019, 10,10))
                .endDate(LocalDate.of(2020, 10,10))
                .autoEnabled(false)
                .build();
        ClassOptionalDateInfo classOptionalDateInfo = classroomMapper.mapFrom(changeClassOptionalDateInfo);

        classroom.changeClassOptionalDateInfo(classOptionalDateInfo);
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroom.toModel().getClassOptionalDateInfo();

        // then
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020,10,10));
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2019,10,10));
        assertFalse(classOptionalDateInfoModel.isAutoEnabled());
    }

    @Test
    @DisplayName("수업 시간 변경")
    void changeClassDateInfo(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        // when
        ChangeClassDateInfo changeClassDateInfo = ChangeClassDateInfo.builder()
                .startHour(1)
                .endHour(6)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .build();
        ClassDateInfo classDateInfo = classroomMapper.mapFrom(changeClassDateInfo);
        classroom.changeClassDateInfo(classDateInfo);
        ClassDateInfoModel classDateInfoModel = classroom.toModel().getClassDateInfo();

        // then
        assertEquals(classDateInfoModel.getDayOfWeek(), DayOfWeek.FRIDAY);
        assertEquals(classDateInfoModel.getEndHour(),6);
        assertEquals(classDateInfoModel.getStartHour(),1);
    }

    @Test
    @DisplayName("비활성화된 수업은 수업시간을 변경할 수 없음")
    void changeClassDateInfo_alreadyDisabled(){
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);
        classroom.disable();

        // when
        assertThrows(IllegalArgumentException.class,()->{
            ChangeClassDateInfo changeClassDateInfo = ChangeClassDateInfo.builder()
                    .dayOfWeek(DayOfWeek.FRIDAY)
                    .startHour(1)
                    .endHour(6)
                    .build();
            ClassDateInfo classDateInfo = classroomMapper.mapFrom(changeClassDateInfo);
            classroom.changeClassDateInfo(classDateInfo);
        });
    }

    private Classroom openClassroom(OpenClassroom openClassroom){
        Classroom classroom = classroomMapper.mapFrom(openClassroom, Register.of("00000000"));
        classroom.open(mock(OpenClassroomValidator.class));
        return classroom;
    }

}
