package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserLoginRequestDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.jwt.TokenDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();;
    }

    @DisplayName("User 로그인 성공")
    @Test
    public void user_로그인_성공() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername";
        UserSignUpRequestDto signUpRequestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(email, password);

        String signUpUrl = "http://localhost:" + port + "/api/v1/auth/signup";
        String loginUrl = "http://localhost:" + port + "/api/v1/auth/login";

        //when
        restTemplate.postForEntity(signUpUrl, signUpRequestDto, CommonResponse.class);
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(loginUrl, loginRequestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

    }

    @DisplayName("회원의 비밀번호가 아니면 로그인 실패")
    @Test
    public void user_로그인_실패() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername";
        UserSignUpRequestDto signUpRequestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(email, "wrongPassword");

        String signUpUrl = "http://localhost:" + port + "/api/v1/auth/signup";
        String loginUrl = "http://localhost:" + port + "/api/v1/auth/login";

        //when
        restTemplate.postForEntity(signUpUrl, signUpRequestDto, CommonResponse.class);
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(loginUrl, loginRequestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }


    @DisplayName("사용자 회원가입 성공")
    @Test
    public void user_회원가입_성공() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();

        String url = "http://localhost:" + port + "/api/v1/auth/signup";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        CommonResponse res = (CommonResponse) responseEntity.getBody();

        Optional<User> user = userRepository.findById(Long.valueOf((Integer) res.getData()));

        assertThat(user.get().getEmail()).isEqualTo(email);
        assertThat(user.get().getPassword()).isNotEqualTo(password);
        assertThat(user.get().getTel()).isEqualTo(tel);
        assertThat(user.get().getUsername()).isEqualTo(username);

    }
}