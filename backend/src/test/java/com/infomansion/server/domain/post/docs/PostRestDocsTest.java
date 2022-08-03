package com.infomansion.server.domain.post.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;
import com.infomansion.server.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.USER_ID;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class PostRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    public void post_작성() throws Exception {
        // given
        Long responseId = 20L;
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .userStuffId(10L)
                .title("Post Title")
                .content("Post Content").build();
        given(postService.createPost(requestDto)).willReturn(responseId);

        // when, then
        mockMvc.perform(post("/api/v1/posts")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userStuffId").description("저장될 UserStuffId"),
                                fieldWithPath("title").description("Post의 제목"),
                                fieldWithPath("content").description("Post의 내용")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(USER_ID.getDescription())))
                ));
    }

    @Test
    public void post의_카테고리와_좋아요를_통해_다른사용자_추천() throws Exception {
        // given
        List<Long> userIds = new ArrayList<>();
        userIds.add(10L);
        userIds.add(20L);
        userIds.add(99L);
        PostRecommendResponseDto responseDto = new PostRecommendResponseDto(userIds);
        given(postService.findRecommendPost()).willReturn(responseDto);

        // when, then
        mockMvc.perform(get("/api/v1/posts/recommend"))
                .andExpect(status().isOk())
                .andDo(document("post-recommend",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터")))
                                .andWithPrefix("data.",
                                        fieldWithPath("userIds").type(JsonFieldType.ARRAY).description("추천된 사용자 Id"))
                ));
    }


}
