package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserLoginRequestDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.apispec.ErrorResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private VerifyEmailService verifyEmailService;

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();;
    }

    @DisplayName("User 로그인 성공")
    @Test
    public void user_로그인_성공() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword1@";
        String tel = "01012345678";
        String username = "testUsername";
        String categories = "IT,COOK";
        UserSignUpRequestDto signUpRequestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
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
        String password = "testPassword1@";
        String tel = "01012345678";
        String username = "testUsername";
        UserSignUpRequestDto signUpRequestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(email, "wrongPassword1@");

        String signUpUrl = "http://localhost:" + port + "/api/v1/auth/signup";
        String loginUrl = "http://localhost:" + port + "/api/v1/auth/login";

        //when
        restTemplate.postForEntity(signUpUrl, signUpRequestDto, CommonResponse.class);
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(loginUrl, loginRequestDto, ErrorResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }


    @DisplayName("비밀번호 형식에 맞지 않는 사용자 회원가입 실패")
    @Test
    public void user_회원가입_실패_잘못된_비밀번호_형식() {
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
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto, ErrorResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        ErrorResponse res = (ErrorResponse) responseEntity.getBody();

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.getCode()).isEqualTo(40031);

    }

    @DisplayName("중복된 이메일인 사용자 회원가입 실패")
    @Test
    public void user_회원가입_실패_중복된_이메일() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword1@";
        String tel = "01012345678";
        String username = "testUsername";
        String categories = "IT";
        UserSignUpRequestDto requestDto1 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        UserSignUpRequestDto requestDto2 = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username("username")
                .categories(categories)
                .build();

        String url = "http://localhost:" + port + "/api/v1/auth/signup";

        //when
        restTemplate.postForEntity(url, requestDto1, ErrorResponse.class);
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto2, ErrorResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        ErrorResponse res = (ErrorResponse) responseEntity.getBody();

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.getCode()).isEqualTo(40002);

    }

    @DisplayName("비밀번호 형식에 맞는 사용자 회원가입 성공")
    @Test
    public void user_올바른_비밀번호_회원가입_성공() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword"+"1"+"$";
        String tel = "01012345678";
        String username = "testUsername";
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        String url = "http://localhost:" + port + "/api/v1/auth/signup";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        CommonResponse res = (CommonResponse) responseEntity.getBody();
        assertThat(res.isSuccess()).isTrue();

        Optional<User> user = userRepository.findById(Long.valueOf((Integer) res.getData()));

        assertThat(user.get().getEmail()).isEqualTo(email);
        assertThat(user.get().getPassword()).isNotEqualTo(password);
        assertThat(user.get().getTel()).isEqualTo(tel);
        assertThat(user.get().getUsername()).isEqualTo(username);

    }

    @DisplayName("올바르지 않은 카테고리를 지정한 사용자는 회원가입을 실패합니다.")
    @Test
    public void 올바르지_않은_카테고리를_지정한_사용자는_회원가입_실패() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword"+"1"+"$";
        String tel = "01012345678";
        String username = "testUsername";
        String categories = "SSAFY,IT";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        String url = "http://localhost:" + port + "/api/v1/auth/signup";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto, ErrorResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        ErrorResponse response = (ErrorResponse) responseEntity.getBody();

        assertThat(response.getCode()).isEqualTo(40040);

    }

    @DisplayName("올바른 카테고리를 지정한 사용자는 회원가입에 성공합니다.")
    @Test
    public void 올바른_카테고리를_지정한_사용자는_회원가입_성공() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword"+"1"+"$";
        String tel = "01012345678";
        String username = "testUsername";
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        String url = "http://localhost:" + port + "/api/v1/auth/signup";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        CommonResponse response = (CommonResponse) responseEntity.getBody();

        assertThat(response.isSuccess()).isTrue();

    }

}