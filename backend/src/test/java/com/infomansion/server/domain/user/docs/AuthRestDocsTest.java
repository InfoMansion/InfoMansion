package com.infomansion.server.domain.user.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.user.dto.UserLoginRequestDto;
import com.infomansion.server.domain.user.dto.UserResetPasswordRequestDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class AuthRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        // given
        Long responseID = 10L;
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
        given(userService.join(any(UserSignUpRequestDto.class))).willReturn(responseID);

        mockMvc.perform(post("/api/v1/auth/signup")
                .content(objectMapper.writeValueAsString(signUpRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("user-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("email"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("tel").description("연락처"),
                                fieldWithPath("categories").description("관심 카테고리")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(USER_ID.getDescription())))
                ));
    }

    @Test
    public void 이메일인증() throws Exception {
        // given
        String requestDto = "infomansion@test.com";
        boolean responseDto = true;
        given(userService.verifiedByEmail(requestDto)).willReturn(responseDto);

        // when, then
        mockMvc.perform(get("/api/v1/auth/verification").param("key", requestDto))
                .andExpect(status().isSeeOther())
                .andDo(document("user-verify-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("key").description("이메일인증 key")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("인증이 성공이나 실패할 경우 이동되는 URL")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부")))
                ));

    }

    @Test
    public void 로그인() throws Exception {
        // given
        String email = "infomansion@test.com";
        String password = "testPassword1@";
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(email, password);
        TokenDto tokenDto = new TokenDto("grantType", "accessToken", "refreshToken", new Date(), 100L);

        given(userService.login(any(UserLoginRequestDto.class))).willReturn(tokenDto);

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("email"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("access, refresh 토큰 정보")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("토큰 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("accessToken").type(ACCESSTOKEN.getJsonFieldType()).description(ACCESSTOKEN.getDescription()),
                                        fieldWithPath("expiresAt").type(ACCESSTOKEN_EXPIRE.getJsonFieldType()).description(ACCESSTOKEN_EXPIRE.getDescription())
                                )
                        )
                );
    }

    @Test
    public void reissue() throws Exception {
        // given
        TokenDto requestDto = new TokenDto("grantType", "accessToken", "refreshToken", new Date(), 100L);
        String infoMansionRefreshToken = "InfoMansionRefreshToken";
        ReissueDto reissueDto = new ReissueDto(requestDto.getAccessToken(), infoMansionRefreshToken);

        TokenDto responseDto = new TokenDto("grantType", "newAccessToken", "newRefreshToken", new Date(), 100L);
        given(userService.reissue(any(ReissueDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(post("/api/v1/auth/reissue")
                        .cookie(new Cookie("InfoMansionRefreshToken", infoMansionRefreshToken))
                        .content(objectMapper.writeValueAsString(reissueDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-reissue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("accessToken").description(ACCESSTOKEN.getDescription()),
                                fieldWithPath("refreshToken").description(REFRESHTOKEN.getDescription())
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("access, refresh 토큰 정보")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("새로운 AccessToken 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("accessToken").type(ACCESSTOKEN.getJsonFieldType()).description(ACCESSTOKEN.getDescription()),
                                        fieldWithPath("expiresAt").type(ACCESSTOKEN_EXPIRE.getJsonFieldType()).description(ACCESSTOKEN_EXPIRE.getDescription())
                                        )
                        )
                );
    }

    @Test
    public void 로그아웃() throws Exception {
        // given
        boolean responseDto = true;
        given(userService.logout()).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andDo(document("user-logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("accessToken과 refreshToken이 삭제되어 보내짐")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("로그아웃 여부")))
                ));
    }

    @Test
    public void 비밀번호찾기() throws Exception {
        //given
        boolean responseDto = true;
        UserResetPasswordRequestDto requestDto = new UserResetPasswordRequestDto("infomansion@test.com", "infomansion");
        given(userService.resetPassword(any(UserResetPasswordRequestDto.class), anyString())).willReturn(responseDto);

        //when&then
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-forgot-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description(USER_EMAIL.getDescription()),
                                fieldWithPath("username").description(USERNAME.getDescription())
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("임시 비밀번호로 초기화 성공 여부")))
                ));
    }
}
