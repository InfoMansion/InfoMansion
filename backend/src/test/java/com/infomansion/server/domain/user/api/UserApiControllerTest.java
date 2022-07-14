package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
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

        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getEmail()).isEqualTo(email);
        assertThat(all.get(0).getPassword()).isEqualTo(password);
        assertThat(all.get(0).getTel()).isEqualTo(tel);
        assertThat(all.get(0).getUsername()).isEqualTo(username);

    }
}