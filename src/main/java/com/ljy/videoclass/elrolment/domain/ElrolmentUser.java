package com.ljy.videoclass.elrolment.domain;

import com.ljy.videoclass.elrolment.domain.infra.ClassroomCodeConverter;
import com.ljy.videoclass.elrolment.domain.infra.RequsterConverter;
import com.ljy.videoclass.elrolment.domain.read.ElrolmentUserModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "elrolment")
@DynamicUpdate
public class ElrolmentUser {
    @Id
    private String identifier;

    @Convert(converter = ClassroomCodeConverter.class)
    private final ClassroomCode code;

    @Convert(converter = RequsterConverter.class)
    private final Requester userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private ElrolmentState state;

    @Column(nullable = false)
    private LocalDate elrolmentDate;

    protected ElrolmentUser(){
        code = null; userId = null;}

    private ElrolmentUser(ClassroomCode code, Requester userId) {
        identifier = UUID.randomUUID().toString();
        this.code = code;
        this.userId = userId;
        state = ElrolmentState.NOT;
        elrolmentDate = LocalDate.now();
    }

    public static ElrolmentUser elrolment(ClassroomCode classroomCode, Requester requester){
        return new ElrolmentUser(classroomCode, requester);
    }

    public void allow() {
        this.state = ElrolmentState.ALLOWED;
    }

    public ElrolmentUserModel toModel(){
        return ElrolmentUserModel.builder()
                .classroomCode(code)
                .requester(userId)
                .state(state)
                .elrolmentDate(elrolmentDate)
                .build();
    }

}
