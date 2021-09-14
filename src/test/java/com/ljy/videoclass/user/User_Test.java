package com.ljy.videoclass.user;

import com.ljy.videoclass.user.command.application.RegisterUserService;
import com.ljy.videoclass.user.command.application.UserRepository;
import com.ljy.videoclass.user.command.application.model.RegisterUser;
import com.ljy.videoclass.user.command.application.UserMapper;
import com.ljy.videoclass.user.domain.*;
import com.ljy.videoclass.user.domain.exception.AlreadyExistUserException;
import com.ljy.videoclass.user.domain.exception.InvalidNameException;
import com.ljy.videoclass.user.domain.exception.InvalidPasswordException;
import com.ljy.videoclass.user.domain.exception.InvalidUserIdException;
import com.ljy.videoclass.user.domain.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class User_Test {

    @Test
    @DisplayName("학번은 반드시 입력해야함")
    void emptyStudentNumber(){
        assertThrows(InvalidUserIdException.class,()->{
            UserId.of("");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"0000000","invalid","00000000 "," 00000000"})
    @DisplayName("학번은 숫자 8자를 입력해야하고 공백은 허용하지 않음")
    void invalidStudentNumber(String value){
        assertThrows(InvalidUserIdException.class,()->{
            UserId.of(value);
        });
    }

    @Test
    void validStudentNumber(){
        UserId studentNumber = UserId.of("00000000");
        assertEquals(studentNumber, UserId.of("00000000"));
        assertEquals("00000000", studentNumber.get());
    }

    @Test
    @DisplayName("비밀번호는 공백을 허용하지 않음")
    void emptyPassword(){
        assertThrows(InvalidPasswordException.class, ()->{
            Password.of("");
        });
    }

    @Test
    void validPassword(){
        Password birth = Password.of("000000");
        assertEquals(birth, Password.of("000000"));
        assertEquals("000000", birth.get());
    }

    @Test
    @DisplayName("이름은 공백을 허용하지 않음")
    void emptyName(){
        assertThrows(InvalidNameException.class, ()->{
           Username.of("");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            "홍길동1",
            "홍길동 ",
            " 홍길동",
            "홍 길 동"
    })
    @DisplayName("이름은 조합형 한글 1자 이상 10자 이하만 허용함")
    void invalidName(String name){
        assertThrows(InvalidNameException.class,()->{
           Username.of(name);
        });
    }

    @Test
    void mapFrom() {
        RegisterUser registerUser = RegisterUser.builder()
                .userId("00000000")
                .password("000000")
                .name("홍길동")
                .build();
        UserMapper userMapper = new UserMapper();
        User user = userMapper.mapFrom(registerUser);
        assertEquals(user.getId().get(), "00000000");
        assertEquals(user.getName().get(), "홍길동");
    }

    @Test
    void alreadyExistUser(){
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existByUserId(UserId.of("00000000")))
                .thenReturn(true);
        RegisterUserValidator validator = new RegisterUserValidator(userRepository);

        assertThrows(AlreadyExistUserException.class,()->{
           validator.validation(UserId.of("00000000"));
        });
    }

    @Nested
    @SpringBootTest
    class RegisterUserService_Test {

        @Autowired
        RegisterUserService registerUserService;

        @Test
        void register(){
            RegisterUser registerUser = RegisterUser.builder()
                    .userId("00000001")
                    .password("000000")
                    .name("홍길동")
                    .build();
            UserModel user = registerUserService.register(registerUser);
            assertNotNull(user);
        }

    }

}
