package com.infomansion.server.domain.user.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.infomansion.server.global.util.restdocs.FieldDescription.USER_ID;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

//    @MockBean
//    private AuthApiController authApiController;

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

//    @SpyBean
//    public TokenDto tokenDto = new TokenDto("grantType", "accessToken", "refreshToken", new Date(), 100L);
//
//
//    @Test
//    public void 로그인() throws Exception {
//        // given
//        String email = "infomansion@test.com";
//        String password = "testPassword1@";
//        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(email, password);
//        TokenDto tokenDto = new TokenDto("grantType", "accessToken", "refreshToken", new Date(), 100L);
//
//        when(userService.login(loginRequestDto)).thenReturn(tokenDto);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
//                        .content(objectMapper.writeValueAsString(loginRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        // then
//        resultActions
//                .andDo(document("user-login",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("email").description("email"),
//                                fieldWithPath("password").description("비밀번호")
//                        )));
//    }
}
