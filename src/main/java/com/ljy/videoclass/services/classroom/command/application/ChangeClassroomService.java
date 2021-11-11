package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.services.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ChangeClassroomService {
    private ClassroomMapper classroomMapper;
    private ClassroomRepository classroomRepository;

    /**
     * @param classroomCode
     * @param register
     * # 수업 활성화
     */
    public ClassroomModel active(ClassroomCode classroomCode, Register register) {
        // find
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);

        // invoke
        classroom.active();
        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("change class into database : {}", classroomModel);
        return classroomModel;
    }

    /**
     * @param classroomCode
     * @param register
     * # 수업 비활성화
     */
    public ClassroomModel disable(ClassroomCode classroomCode, Register register) {
        // find
        Classroom classroom = findByCodeAndRegister(classroomRepository,classroomCode,register);

        // invoke
        classroom.disable();
        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("change class into database : {}", classroomModel);
        return classroomModel;    }

    /**
     * @param changeClassDateInfo
     * @param classroomCode
     * @param register
     * # 수업 날짜 정보 변경
     */
    public ClassroomModel changeClassDateInfo(ChangeClassDateInfo changeClassDateInfo, ClassroomCode classroomCode, Register register) {
        // find
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);

        // map
        ClassDateInfo classDateInfo = classroomMapper.mapFrom(changeClassDateInfo);

        // invoke
        classroom.changeClassDateInfo(classDateInfo);
        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("change class into database : {}", classroomModel);
        return classroomModel;    }

    /**
     * @param changeClassOptionalDateInfo
     * @param classroomCode
     * @param register
     * # 수업 옵션 날짜 정보 변경
     */
    public ClassroomModel changeClassOptionalDateInfo(ChangeClassOptionalDateInfo changeClassOptionalDateInfo,
                                                      ClassroomCode classroomCode,
                                                      Register register) {
        // find
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);

        // map
        ClassOptionalDateInfo classOptionalDateInfo = classroomMapper.mapFrom(changeClassOptionalDateInfo);

        // invoke
        classroom.changeClassOptionalDateInfo(classOptionalDateInfo);
        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("change class into database : {}", classroomModel);
        return classroomModel;
    }

    /**
     * @param changeClassInfo
     * @param classroomCode
     * @param register
     * # 수업 정보 변경
     */
    public ClassroomModel changeClassInfo(ChangeClassInfo changeClassInfo, ClassroomCode classroomCode, Register register) {
        // find
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);

        // map
        ClassInfo classInfo = classroomMapper.mapFrom(changeClassInfo);

        // invoke
        classroom.changeClassInfo(classInfo);
        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("change class into database : {}", classroomModel);
        return classroomModel;
    }
}
