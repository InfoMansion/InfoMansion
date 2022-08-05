package com.infomansion.server.domain.user.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class UserRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void user의_정보를_변경하기_전_인증() throws Exception{
        // given
        UserModifyProfileResponseDto responseDto = UserModifyProfileResponseDto.builder()
                .email("infomansion@test.com").username("infomansion")
                .categories(Arrays.asList("MUSIC","DAILY","NATURE")).introduce("자기소개입니다.").profileImageUrl("imageUrl").build();
        UserAuthRequestDto requestDto = new UserAuthRequestDto("testPassword1$");
        given(userService.authBeforeChangePassword(any(UserAuthRequestDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("user-auth-before-change-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("currentPassword").description("현재 비밀번호").type(JsonFieldType.STRING)
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("사용자 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("email").type(USER_EMAIL.getJsonFieldType()).description(USER_EMAIL.getDescription()),
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("categories").type(USER_CATEGORIES.getJsonFieldType()).description(USER_CATEGORIES.getDescription()),
                                        fieldWithPath("introduce").type(INTRODUCE.getJsonFieldType()).description(INTRODUCE.getDescription()),
                                        fieldWithPath("profileImageUrl").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription())
                                        )
                ));
    }

    @Test
    public void user의_password_변경() throws Exception {
        // given
        Long responseId = 10L;
        UserChangePasswordDto requestDto = new UserChangePasswordDto("newPassword1$");
        given(userService.changePasswordAfterAuth(any(UserChangePasswordDto.class))).willReturn(responseId);

        // when, then
        mockMvc.perform(patch("/api/v1/users/password")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-chang-pw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("newPassword").description("새로운 비밀번호")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(USER_ID.getDescription())))
                ));
    }

    @Test
    public void username으로_사용자정보_조회() throws Exception {
        // given
        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .userId(20L)
                .userEmail("infomansion@test.com")
                .username("infomansion")
                .categories("IT,DAILY,STUDY")
                .profileImage("profileImage")
                .introduce("안녕하세요 infomansion 계정 주인입니다.")
                .build();
        given(userService.findByUsername("infomansion")).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/{username}", "infomansion"))
                .andExpect(status().isOk())
                .andDo(document("user-find-by-username",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("조회할 username")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("사용자 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("userId").type(USER_ID.getJsonFieldType()).description(USER_ID.getDescription()),
                                        fieldWithPath("userEmail").type(USER_EMAIL.getJsonFieldType()).description(USER_EMAIL.getDescription()),
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("categories").type(USER_CATEGORIES.getJsonFieldType()).description(USER_CATEGORIES.getDescription()),
                                        fieldWithPath("profileImage").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription()),
                                        fieldWithPath("introduce").type(INTRODUCE.getJsonFieldType()).description(INTRODUCE.getDescription())
                                )
                ));
    }

    @Test
    public void 사용자의_간단정보_조회() throws Exception {
        // given
        UserSimpleProfileResponseDto responseDto = UserSimpleProfileResponseDto.builder()
                .username("infomansion").profileImage("profileImage").build();
        given(userService.findSimpleProfile()).willReturn(responseDto);

        // when, then
        mockMvc.perform(get("/api/v1/users/info/simple"))
                .andExpect(status().isOk())
                .andDo(document("user-simple-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("사용자의 간단 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("profileImage").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription())
                                )
                ));
    }

    @Test
    public void 사용자_정보_수정() throws Exception {
        // given
        UserModifyProfileResponseDto responseDto = UserModifyProfileResponseDto.builder()
                .email("infomansion@test.com").username("username")
                .categories(Arrays.asList("DAILY","SPORTS")).introduce("자기소개!").profileImageUrl("/modifiedProfileImage").build();
        UserModifyProfileRequestDto  requestDto = new UserModifyProfileRequestDto ("infomansion", "DAILY,SPORTS", "자기소개");

        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MockMultipartFile profileInfo = new MockMultipartFile("profileInfo", "profileInfo", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));
        given(userService.modifyUserProfile(any(MockMultipartFile.class), any(UserModifyProfileRequestDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(multipart(HttpMethod.POST, URI.create("/api/v1/users/profile"))
                        .file(profileImage).file(profileInfo))
                .andExpect(status().isOk())
                .andDo(document("user-modify-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("profileImage").description("수정할 사용자 프로필 이미지"),
                                partWithName("profileInfo").description("수정할 사용자 정보")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("수정된 사용자 정보")))
                                .andWithPrefix("data.",
                                        fieldWithPath("email").type(USER_EMAIL.getJsonFieldType()).description(USER_EMAIL.getDescription()),
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("categories").type(USER_CATEGORIES.getJsonFieldType()).description(USER_CATEGORIES.getDescription()),
                                        fieldWithPath("profileImageUrl").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription()),
                                        fieldWithPath("introduce").type(INTRODUCE.getJsonFieldType()).description(INTRODUCE.getDescription())
                                )
                ));
    }


}
