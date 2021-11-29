package com.ljy.videoclass.services.conferenceHistory.domain;

import com.ljy.videoclass.services.conferenceHistory.domain.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Creator;
import com.ljy.videoclass.services.conferenceHistory.domain.value.LimitCount;
import lombok.Builder;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 회의
 */
@Entity
@Table(name = "conference")
public class Conference extends AbstractAggregateRoot<Conference> {
    // 회의 코드
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "code", length = 50))
    private ConferenceCode code;

    // 회의 제목
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", length = 20, nullable = false))
    private ConferenceTitle title;

    // 회의 제한 인원
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "limit_count", nullable = false))
    private LimitCount limitCount;

    // 회의 개최자
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "creator", nullable = false, length = 15))
    private Creator creator;

    // 회의 생성일
    @Column(nullable = false)
    private LocalDateTime createDateTime;

    protected Conference() {}

    @Builder
    public Conference(ConferenceCode code, ConferenceTitle title, LimitCount limitCount) {
        setCode(code);
        setTitle(title);
        setLimitCount(limitCount);
        createDateTime = LocalDateTime.now();
    }

    private void setCode(ConferenceCode code) {
        if(code == null){
            throw new IllegalArgumentException("회의실 코드를 입력해주세요.");
        }
        this.code = code;
    }

    private void setTitle(ConferenceTitle title) {
        if(title == null){
            throw new IllegalArgumentException("회의실 제목을 입력해주세요.");
        }
        this.title = title;
    }

    private void setLimitCount(LimitCount limitCount) {
        if(limitCount == null){
            throw new IllegalArgumentException("회의 제한 인원을 입력해주세요.");
        }
        this.limitCount = limitCount;
    }

    /**
     * @param openConferenceValidator
     * # 회의 개최
     */
    public void open(Creator creator, OpenConferenceValidator openConferenceValidator) {
        if(creator == null){
            throw new IllegalArgumentException("회의 개최자를 입력해주세요.");
        }
        openConferenceValidator.validation(creator);
        this.creator = creator;
        registerEvent(new OpenedConferenceEvent(code, title, limitCount, creator, createDateTime));
    }

    public ConferenceModel toModel() {
        return ConferenceModel.builder()
                .code(code)
                .creator(creator)
                .limitCount(limitCount)
                .title(title)
                .createDateTime(createDateTime)
                .build();
    }

    @Override
    public String toString() {
        return "Conference{" +
                "code=" + code +
                ", title=" + title +
                ", limitCount=" + limitCount +
                ", creator=" + creator +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
