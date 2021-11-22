package com.ljy.videoclass.services.panelist;


import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import static com.ljy.videoclass.services.panelist.PanelistStatus.ACTIVE;
import static com.ljy.videoclass.services.panelist.PanelistStatus.DE_ACTIVE;

@Slf4j
public class Panelist {
    private Email email;
    private Password password;
    private boolean auth;
    private ExpellCount expellCount;
    private PanelistStatus status;

    @Builder
    public Panelist(Email email, Password password) {
        setEmail(email);
        setPassword(password);
        expellCount = ExpellCount.getInstance();
        this.auth = false;
        this.status = ACTIVE;
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
            status = DE_ACTIVE;
            log.info("de active panelist : {}", email);
        }
    }

    /**
     * 계정 활성화
     */
    public void activation() {
        status = ACTIVE;
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
