package com.ljy.videoclass.services.conferenceHistory.domain;

import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceHistoryModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.HistoryType;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Payload;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 회의 이력
 */
@Entity
@Table(name = "conference_history")
public class ConferenceHistory {

    /**
     * 회의 이력 고유 번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    /**
     * 회의 코드
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "conference_code", length = 50, nullable = false))
    private ConferenceCode code;

    /**
     * 이력 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private HistoryType type;

    /**
     * 이력 내용
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payload", length = 255))
    private Payload payload;

    /**
     * 이력 생성일
     */
    private LocalDateTime createDateTime;

    protected ConferenceHistory(){}

    @Builder
    public ConferenceHistory(ConferenceCode code, HistoryType type, Payload payload) {
        this.code = code;
        this.type = type;
        this.payload = payload;
        this.createDateTime = LocalDateTime.now();
    }

    public ConferenceHistoryModel toModel() {
        return ConferenceHistoryModel.builder()
                .code(code)
                .createDateTime(createDateTime)
                .payload(payload)
                .type(type)
                .build();
    }

    @Override
    public String toString() {
        return "ConferenceHistory{" +
                "seq=" + seq +
                ", code=" + code +
                ", type=" + type +
                ", payload=" + payload +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
