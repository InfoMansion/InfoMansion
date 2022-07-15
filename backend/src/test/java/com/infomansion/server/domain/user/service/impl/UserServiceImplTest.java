package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.util.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 비밀번호_암호화() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();

        //when
        Long userId = userService.join(requestDto);

        //then
        Optional<User> findUser = userRepository.findById(userId);
        assertThat(findUser.get().getPassword()).isNotEqualTo(password);
    }

    @DisplayName("닉네임이 같은 사용자는 회원가입이 불가능합니다.")
    @Test
    public void 중복_회원_검증_닉네임() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        UserSignUpRequestDto requestDto1 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();

        username = "testUsername2";
        UserSignUpRequestDto requestDto2 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();


        //when
        userService.join(requestDto1);

        //then
        assertThatThrownBy(() -> userService.join(requestDto2))
                .isInstanceOf(CustomException.class);

    }

    @DisplayName("이메일이 같은 사용자는 회원가입이 불가능합니다.")
    @Test
    public void 중복_회원_검증_이메일() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        UserSignUpRequestDto requestDto1 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();

        email = "info@test.com";
        UserSignUpRequestDto requestDto2 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();

        //when
        userService.join(requestDto1);

        //then
        assertThatThrownBy(() -> userService.join(requestDto2))
                .isInstanceOf(CustomException.class);

    }
}