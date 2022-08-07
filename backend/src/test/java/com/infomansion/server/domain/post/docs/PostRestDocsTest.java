package com.infomansion.server.domain.post.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostDetailResponseDto;
import com.infomansion.server.domain.post.dto.PostSearchResponseDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSearchResponseDto;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private LikesPostService likesPostService;

    @Test
    public void post_작성() throws Exception {
        // given
        Long responseId = 20L;
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .userStuffId(10L)
                .title("Post Title")
                .content("Post Content").build();
        given(postService.createPost(any(PostCreateRequestDto.class))).willReturn(responseId);

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
    public void userStuffId로_userStuff에_존재하는_모든_post_조회() throws Exception {
        // given
        Long requestDto = 10L;
        List<PostSimpleResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            responseDtoList.add(new PostSimpleResponseDto(Post.builder()
                    .id(requestDto).title("title"+i).content("content"+i)
                    .user(User.builder().build()).userStuff(UserStuff.builder().build()).build()));
        }
        given(postService.findPostByUserStuffId(any(Long.class))).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{userStuffId}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("post-find-by-userstuffid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userStuffId").description("조회할 userStuff Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("조회할 "+ USERSTUFF_ID.getDescription())))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("modifiedDate").type(MODIFIED_DATE.getJsonFieldType()).description(MODIFIED_DATE.getDescription()).optional(),
                                        fieldWithPath("defaultPostThumbnail").type(DEFAULTPOSTTHUMBNAIL.getJsonFieldType()).description(DEFAULTPOSTTHUMBNAIL.getDescription())
                                )
                ));
    }

    @Test
    public void post_상세_조회() throws Exception {
        // given
        Long requestDto = 10L;
        PostDetailResponseDto responseDto = new PostDetailResponseDto(Post.builder()
                .id(requestDto).title("title").content("content")
                .user(User.builder().username("작성자").build())
                .userStuff(UserStuff.builder().category(Category.NONE).build())
                .build());
        given(postService.findPostWithUser(any(Long.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/detail/{postId}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("post-find-by-postid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("조회할 post Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회할 "+ POST_ID.getDescription())))
                                .andWithPrefix("data.",
                                        fieldWithPath("id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("userName").type(USERNAME.getJsonFieldType()).description("작성자 " + USERNAME.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("category").type(CATEGORY.getJsonFieldType()).description("Post의 " + CATEGORY.getDescription()),
                                        fieldWithPath("modifiedDate").type(MODIFIED_DATE.getJsonFieldType()).description(MODIFIED_DATE.getDescription()).optional(),
                                        fieldWithPath("defaultPostThumbnail").type(DEFAULTPOSTTHUMBNAIL.getJsonFieldType()).description(DEFAULTPOSTTHUMBNAIL.getDescription())
                                )
                ));
    }

    @Test
    public void post에_좋아요_추가() throws Exception {
        // given
        Long requestDto = 10L;
        Long responseDto = 10L;
        given(likesPostService.addLikes(any(Long.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/posts/likes/{postId}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("post-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("좋아요를 표시할 postId")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(POST_ID.getDescription())))
                ));
    }

    @Test
    public void post_타이틀_검색() throws Exception {
        // given
        String searchWord = "검색어";

        List<PostSimpleResponseDto> postsByTitleOrContent = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            postsByTitleOrContent.add(new PostSimpleResponseDto(Post.builder()
                    .id(Long.valueOf(i)).title("title"+i).content("content"+i)
                    .user(User.builder().build()).userStuff(UserStuff.builder().build())
                    .build()));
        }
        Slice<PostSimpleResponseDto> postByTitle = new SliceImpl<>(postsByTitleOrContent, Pageable.ofSize(10), true);
        PostSearchResponseDto titleResponse = new PostSearchResponseDto(postByTitle);
        given(postService.findPostBySearchWordForTitle(anyString(), any(Pageable.class))).willReturn(titleResponse);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/search/title?searchWord=", searchWord))
                .andExpect(status().isOk())
                .andDo(document("post-search-title",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("searchWord").description("검색어")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.VARIES).description("타이틀 검색 결과")))
                                .andWithPrefix("data.postsByTitleOrContent.",
                                        fieldWithPath("content[]").type(SLICE_CONTENT.getJsonFieldType()).description(SLICE_CONTENT.getDescription()),
                                        fieldWithPath("content.[].id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("content.[].title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content.[].content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("content.[].defaultPostThumbnail").type(JsonFieldType.STRING).description("대표 이미지 사진"),
                                        fieldWithPath("content.[].likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("numberOfElements").type(SLICE_NUMBER_OF_ELEMENTS.getJsonFieldType()).description(SLICE_NUMBER_OF_ELEMENTS.getDescription()),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())
                                )

                ));
    }

    @Test
    public void post_content_검색() throws Exception {
        // given
        String searchWord = "검색어";
        List<PostSimpleResponseDto> postsByTitleOrContent = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            postsByTitleOrContent.add(new PostSimpleResponseDto(Post.builder()
                    .id(Long.valueOf(i)).title("title"+i).content("content"+i)
                    .user(User.builder().build()).userStuff(UserStuff.builder().build())
                    .build()));
        }
        Slice<PostSimpleResponseDto> postsByContent = new SliceImpl<>(postsByTitleOrContent, Pageable.ofSize(10), false);
        PostSearchResponseDto contentResponse = new PostSearchResponseDto(postsByContent);
        given(postService.findPostBySearchWordForContent(anyString(), any(Pageable.class))).willReturn(contentResponse);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/search/content?searchWord=검색어"))
                .andExpect(status().isOk())
                .andDo(document("post-search-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("searchWord").description("검색어")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.VARIES).description("post 제목으로 검색한 결과")))
                                .andWithPrefix("data.postsByTitleOrContent.",
                                        fieldWithPath("content[]").type(SLICE_CONTENT.getJsonFieldType()).description(SLICE_CONTENT.getDescription()),
                                        fieldWithPath("content.[].id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("content.[].title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content.[].content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("content.[].defaultPostThumbnail").type(JsonFieldType.STRING).description("대표 이미지 사진"),
                                        fieldWithPath("content.[].likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("numberOfElements").type(SLICE_NUMBER_OF_ELEMENTS.getJsonFieldType()).description(SLICE_NUMBER_OF_ELEMENTS.getDescription()),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())
                                )

                ));
    }


}
