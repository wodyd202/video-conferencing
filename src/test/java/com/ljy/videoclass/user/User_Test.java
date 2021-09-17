package com.ljy.videoclass.user;

import com.ljy.videoclass.user.domain.OauthLoginUser;
import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.exception.InvalidEmailException;
import com.ljy.videoclass.user.domain.exception.InvalidImageException;
import com.ljy.videoclass.user.domain.exception.InvalidNameException;
import com.ljy.videoclass.user.domain.exception.InvalidUserIdException;
import com.ljy.videoclass.user.domain.read.UserInfoModel;
import com.ljy.videoclass.user.domain.read.UserModel;
import com.ljy.videoclass.user.domain.value.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class User_Test {

    @Test
    @DisplayName("사용자 고유 아이디는 반드시 입력해야함")
    void empty_userId(){
        assertThrows(InvalidUserIdException.class, ()->{
            UserId.of("");
        });
    }

    @Test
    @DisplayName("사용자 고유 아이디 정상 입력")
    void valid_userId(){
        UserId userId = UserId.of("사용자아이디");
        assertEquals(userId, UserId.of("사용자아이디"));
        assertEquals(userId.get(), "사용자아이디");
    }

    @Test
    @DisplayName("사용자 이름은 반드시 입력해야함")
    void empty_username(){
        assertThrows(InvalidNameException.class, ()->{
            Username.of("");
        });
    }

    @Test
    @DisplayName("사용자 이름 정상 입력")
    void valid_username(){
        Username username = Username.of("홍길동");
        assertEquals(username, Username.of("홍길동"));
        assertEquals(username.get(), "홍길동");
    }

    @Test
    @DisplayName("사용자 이메일은 반드시 입력해야함")
    void empty_email(){
        assertThrows(InvalidEmailException.class, ()->{
            Email.of("");
        });
    }

    @Test
    @DisplayName("사용자 이메일 정상 입력")
    void valid_email(){
        Email email = Email.of("test@google.com");
        assertEquals(email, Email.of("test@google.com"));
        assertEquals(email.get(), "test@google.com");
    }

    @Test
    @DisplayName("사용자 이미지는 반드시 입력해야함")
    void empty_image(){
        assertThrows(InvalidImageException.class,()->{
            Image.path("");
        });
    }

    @Test
    void userInfo(){
        UserInfo userInfo = UserInfo.builder()
                .email(Email.of("email@google.com"))
                .username(Username.of("홍길동"))
                .image(Image.path("path"))
                .build();

        UserInfoModel userInfoModel = userInfo.toModel();
        assertEquals(userInfoModel.getUsername(), "홍길동");
        assertEquals(userInfoModel.getEmail(), "email@google.com");
        assertEquals(userInfoModel.getImage(), "path");
    }

    @Test
    void mapfrom(){
        OauthLoginUser loginUser = OauthLoginUser.builder()
                .email("email@google.com")
                .identifier("identifier")
                .image("imagePath")
                .username("홍길동")
                .build();
        User user = User.oauthLogin(loginUser);
        UserModel userModel = user.toModel();
        assertEquals(userModel.getUserId(), "identifier");
        assertEquals(userModel.getUserInfo().getImage(), "imagePath");
        assertEquals(userModel.getUserInfo().getUsername(), "홍길동");
        assertEquals(userModel.getUserInfo().getEmail(),"email@google.com");
    }

}
