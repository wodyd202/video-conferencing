package com.ljy.videoclass.user.domain;

import com.ljy.videoclass.user.domain.read.UserModel;
import com.ljy.videoclass.user.domain.value.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "users")
@DynamicUpdate
public class User {

    @EmbeddedId
    private final UserId id;

    @Embedded
    private UserInfo userInfo;

    protected User(){id = null;}

    @Builder
    public User(UserId id, UserInfo userInfo) {
        this.id = id;
        this.userInfo = userInfo;
    }

    public static User oauthLogin(OauthLoginUser loginUser) {
        return User.builder()
                .id(UserId.of(loginUser.getIdentifier()))
                .userInfo(UserInfo.builder()
                        .image(loginUser.getImage() != null ? Image.path(loginUser.getImage()) : null)
                        .username(Username.of(loginUser.getUsername()))
                        .email(Email.of(loginUser.getEmail()))
                        .build())
                .build();
    }

    public void synchronize(User user) {
        this.userInfo = user.userInfo;
    }

    public UserModel toModel(){
        return UserModel.builder()
                .userId(id.get())
                .userInfo(userInfo.toModel())
                .build();
    }
}
