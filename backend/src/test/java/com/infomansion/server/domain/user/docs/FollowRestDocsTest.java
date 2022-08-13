package com.infomansion.server.domain.user.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.user.dto.UserFollowInfoResponseDto;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import com.infomansion.server.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class FollowRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 다른_사용자_팔로우() throws Exception {
        // given
        boolean response = true;
        String request = "infomansion";
        given(userService.followUser(anyString())).willReturn(response);

        // when
        mockMvc.perform(post("/api/v1/follow/{username}", request))
                .andExpect(status().isOk())
                .andDo(document("follow-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("팔로우할 username")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("팔로우 성공")))
                ));
    }

    @Test
    public void 다른_사용자_언팔로우() throws Exception {
        // given
        boolean response = true;
        String request = "infomansion";
        given(userService.followUser(anyString())).willReturn(response);

        // when
        mockMvc.perform(delete("/api/v1/follow/{username}", request))
                .andExpect(status().isOk())
                .andDo(document("unfollow-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("언팔로우할 username")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("언팔로우 성공")))
                ));
    }

    @Test
    public void 사용자의_팔로잉_리스트() throws Exception {
        // given
        String request = "infomansion";
        List<UserFollowInfoResponseDto> response = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            response.add(UserFollowInfoResponseDto.builder()
                    .username("username"+i).profileImage("profileImage").followFlag(true).build());
        }
        given(userService.findFollowingUserList(anyString())).willReturn(response);

        // when
        mockMvc.perform(get("/api/v1/follow/following/{username}", request))
                .andExpect(status().isOk())
                .andDo(document("following-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("username의 팔로잉 목록")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("팔로잉 목록")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("profileImage").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription()),
                                        fieldWithPath("followFlag").type(JsonFieldType.BOOLEAN).description("로그인한 사용자가 해당 user를 팔로우 했는지 구별"))
                ));
    }

    @Test
    public void 사용자의_팔로워_리스트() throws Exception {
        // given
        String request = "infomansion";
        List<UserFollowInfoResponseDto> response = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            response.add(UserFollowInfoResponseDto.builder()
                    .username("username"+i).profileImage("profileImage").followFlag(true).build());
        }
        given(userService.findFollowerUserList(anyString())).willReturn(response);

        // when
        mockMvc.perform(get("/api/v1/follow/follower/{username}", request))
                .andExpect(status().isOk())
                .andDo(document("follower-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("username의 팔로워 목록")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("팔로워 목록")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("profileImage").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription()),
                                        fieldWithPath("followFlag").type(JsonFieldType.BOOLEAN).description("로그인한 사용자가 해당 user를 팔로우 했는지 구별"))
                ));
    }
}
