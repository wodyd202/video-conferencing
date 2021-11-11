package com.ljy.videoclass.services.user.domain.value;

import com.ljy.videoclass.services.user.domain.infra.EmailConverter;
import com.ljy.videoclass.services.user.domain.infra.ImageConverter;
import com.ljy.videoclass.services.user.domain.infra.UsernameConverter;
import com.ljy.videoclass.services.user.domain.read.UserInfoModel;
import lombok.Builder;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserInfo {
    @Convert(converter = EmailConverter.class)
    private final Email email;
    @Convert(converter = UsernameConverter.class)
    private Username username;
    @Convert(converter = ImageConverter.class)
    private Image image;

    protected UserInfo(){
        email = null;
    }

    /**
     * @param email 사용자 이메일
     * @param username 사용자 이름
     * @param image 사용자 프로필 사진
     */
    @Builder
    private UserInfo(Email email, Username username, Image image) {
        this.email = email;
        this.username = username;
        this.image = image;
    }

    public UserInfoModel toModel() {
        return UserInfoModel.builder()
                .email(email.get())
                .image(image.get())
                .username(username.get())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(email, userInfo.email) && Objects.equals(username, userInfo.username) && Objects.equals(image, userInfo.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, image);
    }
}
