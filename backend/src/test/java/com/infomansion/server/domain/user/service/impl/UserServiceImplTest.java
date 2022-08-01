package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserChangeCategoriesDto;
import com.infomansion.server.domain.user.dto.UserInfoResponseDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private VerifyEmailService verifyEmailService;

    @BeforeEach
    public void setUp() {
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build());
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();;
    }

    @Test
    public void 비밀번호_암호화() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
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
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when & then
        assertThatThrownBy(() -> userService.join(requestDto))
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
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when & then
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(CustomException.class);

    }



    @WithCustomUserDetails
    @Test
    public void 카테고리_수정() {
        //given
        UserChangeCategoriesDto changeCategoriesDto = new UserChangeCategoriesDto("FASHIONANDBEAUTY");

        //when
        Long userId = userService.changeCategories(changeCategoriesDto);

        //then
        User user = userRepository.findById(userId).get();

        assertThat(user.getCategories()).isEqualTo("FASHIONANDBEAUTY");
    }

    @Test
    public void 회원가입시_기본프로필이미지제공() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when
        Long userId = userService.join(requestDto);

        //then
        assertThat(userRepository.findById(userId).get().getProfileImage()).isEqualTo("/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png");
    }

    @Test
    public void 사용자이름으로_회원조회시_UserInfoResponseDto_반환 () {
        //given&when
        UserInfoResponseDto responseDto = userService.findByUsername("infomansion");

        //then
        assertThat(responseDto.getUsername()).isEqualTo("infomansion");
        assertThat(responseDto.getCategories()).isEqualTo("IT,COOK");
        assertThat(responseDto.getProfileImage()).isNotNull();
        assertThat(responseDto.getUserId()).isNotNull();
        assertThat(responseDto.getIntroduce()).isNotNull();

    }

}