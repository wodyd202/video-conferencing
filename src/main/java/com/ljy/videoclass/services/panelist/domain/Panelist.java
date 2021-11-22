package com.ljy.videoclass.services.panelist.domain;


import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelist;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.domain.value.Email;
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
    // 이메일
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "email", length = 50))
    private Email email;

    // 비밀번호
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", length = 100, nullable = false))
    private Password password;

    // 이메일 인증 상태
    @Column(nullable = false, length = 5)
    private boolean auth;

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
    public Panelist(Email email, Password password) {
        setEmail(email);
        setPassword(password);
        expellCount = ExpellCount.getInstance();
        this.auth = false;
        this.status = PanelistStatus.ACTIVE;
        registerEvent(SignUpedPanelist.builder()
                .auth(auth)
                .email(email)
                .expellCount(expellCount)
                .password(password)
                .status(status)
                .build());
    }

    private void setEmail(Email email) {
        if(email == null){
            throw new IllegalArgumentException("회의자의 이메일을 입력해주세요.");
        }
        this.email = email;
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
        expellCount.increment();
        log.info("expell panelist : {}", email);

        // 추방 횟수가 5회가 된 경우 계정 비활성화함
        if(expellCount.isMaxium()){
            expellCount = ExpellCount.getInstance();
            status = PanelistStatus.DE_ACTIVE;
            log.info("de active panelist : {}", email);
        }
    }

    /**
     * 계정 활성화
     */
    public void activation() {
        status = PanelistStatus.ACTIVE;
        log.info("active panelist : {}", email);
    }

    // to model
    public PanelistModel toModel() {
        return PanelistModel.builder()
                .email(email)
                .auth(auth)
                .status(status)
                .expellCount(expellCount)
                .build();
    }

    @Override
    public String toString() {
        return "Panelist{" +
                "email=" + email +
                ", password=" + password +
                ", auth=" + auth +
                ", expellCount=" + expellCount +
                ", status=" + status +
                '}';
    }
}
