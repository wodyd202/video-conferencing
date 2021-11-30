package com.ljy.videoclass.services.panelist.domain;


import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelistEvent;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.ExpellCount;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/**
 * 회의자
 */
@Slf4j
@Entity
@Table(name = "panelist")
@DynamicUpdate
public class Panelist extends AbstractAggregateRoot<Panelist> {
    // 아이디
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", length = 15, nullable = false))
    private PanelistId id;

    // 비밀번호
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", length = 100, nullable = false))
    private Password password;

    // 추방 횟수
    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "expell_count", nullable = false))
    private ExpellCount expellCount;

    // 회의자 상태(활성화, 비활성화)
    // 추방 횟수가 5회가 되면 비활성화
    @Enumerated(STRING)
    @Column(nullable = false, length = 9)
    private PanelistStatus status;

    protected Panelist(){}

    @Builder
    public Panelist(PanelistId id, Password password) {
        setId(id);
        setPassword(password);
        expellCount = ExpellCount.getInstance();
        this.status = PanelistStatus.ACTIVE;
        registerEvent(SignUpedPanelistEvent.builder()
                .id(id)
                .expellCount(expellCount)
                .password(password)
                .status(status)
                .build());
    }

    private void setId(PanelistId panelistId) {
        if(panelistId == null){
            throw new IllegalArgumentException("회의자의 아이디를 입력해주세요.");
        }
        this.id = panelistId;
    }

    private void setPassword(Password password) {
        if(password == null){
            throw new IllegalArgumentException("회의자의 비밀번호를 입력해주세요.");
        }
        this.password = password;
    }

    /**
     * 추방
     */
    public void expell() {
        expellCount = expellCount.increment();
        log.info("expell panelist : {}", id);

        // 추방 횟수가 5회가 된 경우 계정 비활성화함
        if(expellCount.isMaxium()){
            expellCount = ExpellCount.getInstance();
            status = PanelistStatus.DE_ACTIVE;
            log.info("de active panelist : {}", id);
        }
    }

    /**
     * 계정 활성화
     */
    public void activation() {
        status = PanelistStatus.ACTIVE;
        log.info("active panelist : {}", id);
    }

    // to model
    public PanelistModel toModel() {
        return PanelistModel.builder()
                .id(id)
                .status(status)
                .expellCount(expellCount)
                .build();
    }

    @Override
    public String toString() {
        return "Panelist{" +
                "id=" + id +
                ", password=" + password +
                ", expellCount=" + expellCount +
                ", status=" + status +
                '}';
    }


}
