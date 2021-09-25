package com.ljy.videoclass.elrolment.domain;

import com.ljy.videoclass.elrolment.domain.infra.ClassroomCodeConverter;
import com.ljy.videoclass.elrolment.domain.infra.RequsterConverter;
import com.ljy.videoclass.elrolment.domain.read.ElrolmentUserModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import com.ljy.videoclass.elrolment.domain.value.RequesterInfo;
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

    @Embedded
    private final RequesterInfo requesterInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private ElrolmentState state;

    @Column(nullable = false)
    private LocalDate elrolmentDate;

    protected ElrolmentUser(){
        code = null; requesterInfo = null;}

    private ElrolmentUser(ClassroomCode code, RequesterInfo requesterInfo) {
        identifier = UUID.randomUUID().toString();
        this.code = code;
        this.requesterInfo = requesterInfo;
        state = ElrolmentState.NOT;
        elrolmentDate = LocalDate.now();
    }

    public static ElrolmentUser elrolment(ClassroomCode classroomCode, RequesterInfo requester){
        return new ElrolmentUser(classroomCode, requester);
    }

    public void allow() {
        this.state = ElrolmentState.ALLOWED;
    }

    public ElrolmentUserModel toModel(){
        return ElrolmentUserModel.builder()
                .classroomCode(code)
                .requester(requesterInfo)
                .state(state)
                .elrolmentDate(elrolmentDate)
                .build();
    }

}
