package com.infomansion.server.domain.post.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.post.service.UserLikePostService;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.dto.UserModifyProfileRequestDto;
import com.infomansion.server.domain.user.dto.UserModifyProfileResponseDto;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private UserLikePostService userLikePostService;

    @MockBean
    private S3Uploader s3Uploader;

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
                                fieldWithPath("content").description("Post의 내용"),
                                fieldWithPath("images").description("업로드한 이미지 주소들")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(USER_ID.getDescription())))
                ));
    }

    @Test
    public void userStuffId로_userStuff에_존재하는_모든_post_조회() throws Exception {
        // given
        Long requestDto = 10L;
        List<PostSimpleResponseDto> postsByUserStuffId = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            postsByUserStuffId.add(PostSimpleResponseDto.builder()
                    .id((long) i)
                    .title("title"+i)
                    .content("content"+i)
                    .category(new CategoryMapperValue(Category.DAILY))
                    .defaultPostThumbnail("default")
                    .modifiedDate(LocalDateTime.now())
                    .likes(2L)
                    .build());
        }

        Slice<PostSimpleResponseDto> postByUserStuffId = new SliceImpl<>(postsByUserStuffId,Pageable.ofSize(10), true);
        PostbyUserStuffResponseDto responseDto = new PostbyUserStuffResponseDto(postByUserStuffId);

        given(postService.findPostByUserStuffId(any(Long.class), any(Pageable.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{userStuffId}", requestDto)
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-find-by-userstuffid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userStuffId").description("조회할 userStuff Id")
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지의 번호"),
                                parameterWithName("size").description("한 번에 보여줄 데이터의 개수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회할 "+ USERSTUFF_ID.getDescription())))
                                .andWithPrefix("data.postsByUserStuff.",
                                        fieldWithPath("content.[].id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("content.[].title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content.[].content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("content.[].likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("content.[].category").type(JsonFieldType.OBJECT).description(CATEGORY.getDescription()),
                                        fieldWithPath("content.[].category.category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                                        fieldWithPath("content.[].category.categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription()),
                                        fieldWithPath("content.[].modifiedDate").type(MODIFIED_DATE.getJsonFieldType()).description(MODIFIED_DATE.getDescription()).optional(),
                                        fieldWithPath("content.[].defaultPostThumbnail").type(DEFAULTPOSTTHUMBNAIL.getJsonFieldType()).description(DEFAULTPOSTTHUMBNAIL.getDescription()),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())                                )
                ));
    }

    @Test
    public void username의_postbox에_존재하는_모든_post_조회() throws Exception {
        // given
        Long requestDto = 10L;
        List<PostSimpleResponseDto> postsByUserStuffId = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            postsByUserStuffId.add(PostSimpleResponseDto.builder()
                    .id((long) i)
                    .title("title"+i)
                    .content("content"+i)
                    .category(new CategoryMapperValue(Category.DAILY))
                    .defaultPostThumbnail("default")
                    .modifiedDate(LocalDateTime.now())
                    .likes(2L)
                    .build());
        }

        Slice<PostSimpleResponseDto> responseDto = new SliceImpl<>(postsByUserStuffId,Pageable.ofSize(10), true);
        given(postService.findPostInThePostbox(anyString(), any(Pageable.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/postbox/{username}", requestDto)
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-find-post-in-the-postbox",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description(USERNAME.getDescription())
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지의 번호"),
                                parameterWithName("size").description("한 번에 보여줄 데이터의 개수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("Postbox에 들어있는 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("content.[].id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("content.[].title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content.[].content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("content.[].likes").type(LIKES_POST.getJsonFieldType()).description(LIKES_POST.getDescription()),
                                        fieldWithPath("content.[].category").type(JsonFieldType.OBJECT).description(CATEGORY.getDescription()),
                                        fieldWithPath("content.[].category.category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                                        fieldWithPath("content.[].category.categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription()),
                                        fieldWithPath("content.[].modifiedDate").type(MODIFIED_DATE.getJsonFieldType()).description(MODIFIED_DATE.getDescription()).optional(),
                                        fieldWithPath("content.[].defaultPostThumbnail").type(DEFAULTPOSTTHUMBNAIL.getJsonFieldType()).description(DEFAULTPOSTTHUMBNAIL.getDescription()),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())                                )
                ));
    }

    @Test
    public void username의_guestbook에_존재하는_모든_post_조회() throws Exception {
        // given
        Long requestDto = 10L;
        List<PostGuestBookResponseDto> postsByUserStuffId = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            postsByUserStuffId.add(PostGuestBookResponseDto.builder()
                    .id((long) i)
                    .username("username"+i)
                    .content("content"+i)
                    .modifiedDate(LocalDateTime.now())
                    .build());
        }
        Slice<PostGuestBookResponseDto> responseDto = new SliceImpl<>(postsByUserStuffId,Pageable.ofSize(10), true);
        given(postService.findPostInTheGuestbook(anyString(), any(Pageable.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/guestbook/{username}", requestDto)
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-find-post-in-the-guestbook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description(USERNAME.getDescription())
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지의 번호"),
                                parameterWithName("size").description("한 번에 보여줄 데이터의 개수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("Guestbook에 들어있는 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("content.[].id").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("content.[].username").type(USERNAME.getJsonFieldType()).description("작성자"),
                                        fieldWithPath("content.[].content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("content.[].modifiedDate").type(MODIFIED_DATE.getJsonFieldType()).description(MODIFIED_DATE.getDescription()).optional(),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())                                )
                ));
    }

    @Test
    public void post_상세_조회() throws Exception {
        // given
        Long requestDto = 10L;
        PostDetailResponseDto responseDto = PostDetailResponseDto.builder()
                .id(10L)
                .title("title")
                .userName("username")
                .content("content")
                .category(Category.NONE)
                .defaultPostThumbnail("default")
                .modifiedDate(LocalDateTime.now())
                .likes(10L)
                .followFlag(false)
                .likeFlag(false)
                .build();
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
                                        fieldWithPath("defaultPostThumbnail").type(DEFAULTPOSTTHUMBNAIL.getJsonFieldType()).description(DEFAULTPOSTTHUMBNAIL.getDescription()),
                                        fieldWithPath("followFlag").type(FOLLOW_FLAG.getJsonFieldType()).description(FOLLOW_FLAG.getDescription()),
                                        fieldWithPath("likeFlag").type(LIKE_FLAG.getJsonFieldType()).description(LIKE_FLAG.getDescription())
                                )
                ));
    }

    @Test
    public void post에_좋아요_추가() throws Exception {
        // given
        Long requestDto = 10L;
        Long responseDto = 10L;
        given(userLikePostService.likePost(any(Long.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts/likes/{postId}", requestDto))
                .andExpect(status().isCreated())
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
            postsByTitleOrContent.add(PostSimpleResponseDto.builder()
                    .id((long) i)
                    .title("title"+i)
                    .content("content"+i)
                    .defaultPostThumbnail("default")
                    .modifiedDate(LocalDateTime.now())
                    .likes(2L)
                    .build());
        }
        Slice<PostSimpleResponseDto> postByTitle = new SliceImpl<>(postsByTitleOrContent, Pageable.ofSize(10), true);
        PostSearchResponseDto titleResponse = new PostSearchResponseDto(postByTitle);
        given(postService.findPostBySearchWordForTitle(anyString(), any(Pageable.class))).willReturn(titleResponse);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/search/title?searchWord=", searchWord)
                .param("page", "1")
                .param("size", "3"))
                .andExpect(status().isOk())
                .andDo(document("post-search-title",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("searchWord").description("검색어"),
                                parameterWithName("page").description("조회할 페이지의 번호"),
                                parameterWithName("size").description("한 번에 보여줄 데이터의 개수")
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
            postsByTitleOrContent.add(PostSimpleResponseDto.builder()
                    .id((long) i)
                    .title("title"+i)
                    .content("content"+i)
                    .defaultPostThumbnail("default")
                    .modifiedDate(LocalDateTime.now())
                    .likes(2L)
                    .build());
        }
        Slice<PostSimpleResponseDto> postsByContent = new SliceImpl<>(postsByTitleOrContent, Pageable.ofSize(10), false);
        PostSearchResponseDto contentResponse = new PostSearchResponseDto(postsByContent);
        given(postService.findPostBySearchWordForContent(anyString(), any(Pageable.class))).willReturn(contentResponse);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/search/content?searchWord=검색어")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andDo(document("post-search-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("searchWord").description("검색어"),
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지에서 조회할 데이터 개수")
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

    @Test
    public void post_좋아요() throws Exception {
        // given
        Long requestDto = 10L;
        Long responseDto = 10L;
        given(userLikePostService.likePost(anyLong())).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts/likes/{postId}", requestDto))
                .andExpect(status().isCreated())
                .andDo(document("user-likes-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("좋아요를 누를 Post의 id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(POST_ID.getDescription())))
                ));
    }

    @Test
    public void 사용자가_좋아요를_누른_Post_목록_조회() throws Exception {
        //given
        List<PostSimpleResponseDto> posts = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            posts.add(
                    PostSimpleResponseDto.builder()
                            .id((long) i)
                            .title("title"+i)
                            .content("content"+i)
                            .category(new CategoryMapperValue(Category.DAILY))
                            .defaultPostThumbnail("default")
                            .modifiedDate(LocalDateTime.now())
                            .likes(2L)
                            .build());
        }
        Slice<PostSimpleResponseDto> responseDtos = new SliceImpl<>(posts);
        given(userLikePostService.findPostsUserLikes(any(Pageable.class))).willReturn(responseDtos);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v2/posts/my-likes")
                        .param("size", "10")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andDo(document("post-list-user-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지에서 조회할 데이터 개수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("사용자가 좋아요를 누른 Post 목록")))
                                .andWithPrefix("data..",
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
    public void Post에_좋아요를_누른_사용자_목록_조회() throws Exception {
        //given
        List<UserSimpleProfileResponseDto> responseDtoList = new ArrayList<>();
        for(int i=1;i<=5;i++) {
            responseDtoList.add(UserSimpleProfileResponseDto.builder()
                    .username("username" + i)
                    .profileImage("profileImage" + i)
                    .build());
        }

        given(userLikePostService.findUsersLikeThisPost(anyLong())).willReturn(responseDtoList);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v2/posts/likes/{postId}", 10L))
                .andExpect(status().isOk())
                .andDo(document("users-like-this-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("사용자들이 좋아요를 누른 Post의 id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("해당 Post를 좋아요 누른 사용자 목록")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("username").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("profileImage").type(PROFILE_IMAGE.getJsonFieldType()).description(PROFILE_IMAGE.getDescription())
                                )

                ));
    }

    @Test
    public void postId로_Post_삭제() throws Exception {
        // given
        Long requestDto = 10L;
        Boolean response = true;
        given(postService.deletePost(any(Long.class))).willReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/posts/{postId}", requestDto))
                .andExpect(status().isOk())
                .andDo(document("post-delete-by-post-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("삭제할 post Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("삭제 성공 여부")))
                ));
    }

    @Test
    public void post_수정() throws Exception {
        // given
        PostModifyRequestDto requestDto = PostModifyRequestDto.builder()
                .postId(20L)
                .userStuffId(10L)
                .title("Post Title")
                .content("Post Content")
                .images(new ArrayList<>()).build();
        given(postService.modifyPostAndSaveAsTemp(any(PostModifyRequestDto.class))).willReturn(true);

        // when, then
        mockMvc.perform(put("/api/v1/posts")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").description("수정할 postId"),
                                fieldWithPath("userStuffId").description("저장될 UserStuffId"),
                                fieldWithPath("title").description("Post의 제목"),
                                fieldWithPath("content").description("Post의 내용"),
                                fieldWithPath("images").description("업로드한 이미지 주소들")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("수정 성공 여부")))
                ));
    }

    @Test
    public void post_작성_취소_시_업로드_되었던_image_삭제() throws Exception {
        // given
        List<String> deleteImages = new ArrayList<>();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts/reset")
                .content(objectMapper.writeValueAsString(deleteImages))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-reset",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("[]").description("삭제할 image Url")
                        )
                ));
    }

    @Test
    public void post를_임시저장하면서_이미지를_업로드() throws Exception {
        // given
        TempPostImageUploadResponseDto responseDto = new TempPostImageUploadResponseDto(10L, "임시저장된 title", "임시저장된 content", "임시저장된 imgUrl");

        TempPostSaveRequestDto requestDto = new TempPostSaveRequestDto("임시저장할 title", "임시저장할 content");
        MockMultipartFile image = new MockMultipartFile("image", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        MockMultipartFile tempPost = new MockMultipartFile("post", "post", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));
        given(postService.createTempPostAndUploadImage(any(MockMultipartFile.class), any(TempPostSaveRequestDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(multipart(HttpMethod.POST, URI.create("/api/v2/posts/upload/temp"))
                        .file(image).file(tempPost))
                .andExpect(status().isCreated())
                .andDo(document("post-create-temp-post-and-upload-image-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("임시저장할 image"),
                                partWithName("post").description("임시저장할 title과 content를 담아주세요")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("임시저장된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("imgUrl").type(JsonFieldType.STRING).description("임시저장된 image url")
                                )
                ));
    }

    @Test
    public void post를_임시저장() throws Exception {
        // given
        TempPostSaveResponseDto responseDto = new TempPostSaveResponseDto(10L, LocalDateTime.now(), "임시저장된 title", "임시저장된 content");
        TempPostSaveRequestDto requestDto = new TempPostSaveRequestDto("임시저장할 title", "임시저장할 content");
        given(postService.createTempPost(any(TempPostSaveRequestDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts/temp")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-create-temp-post-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("임시저장할 title"),
                                fieldWithPath("content").description("임시저장할 content")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("임시저장된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("임시저장된 시간"),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription())
                                )
                ));
    }

    @Test
    public void 새로_작성하는_post_발생() throws Exception {
        // given
        PostSaveRequestDto requestDto = new PostSaveRequestDto(10L, "post title", "post content");
        PostSaveResponseDto responseDto = new PostSaveResponseDto(10L, 20L, "발행된 title", "발행된 content");
        given(postService.createNewPost(any(PostSaveRequestDto.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-create-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userStuffId").description("저장될 UserStuffId"),
                                fieldWithPath("title").description("title"),
                                fieldWithPath("content").description("content")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("발행된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description("post가 저장되는 UserStuffId"),
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription())
                                )
                ));
    }

    @Test
    public void post를_수정하고_임시저장하고_이미지_업로드() throws Exception {
        // given
        TempPostImageUploadResponseDto responseDto = new TempPostImageUploadResponseDto(10L, "임시저장된 title", "임시저장된 content", "임시저장된 imgUrl");

        Long postId = 10L;
        MockMultipartFile image = new MockMultipartFile("image", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        TempPostSaveRequestDto requestDto = new TempPostSaveRequestDto("임시저장할 title", "임시저장할 content");
        MockMultipartFile tempPost = new MockMultipartFile("post", "post", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));
        given(postService.modifyPostAndImageUpload(any(MockMultipartFile.class), any(TempPostSaveRequestDto.class), anyLong())).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/v2/posts/upload/{postId}", postId)
                        .file(image).file(tempPost))
                .andExpect(status().isCreated())
                .andDo(document("post-modify-temp-post-or-post-and-upload-image-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("임시저장할 postId")
                        ),
                        requestParts(
                                partWithName("image").description("임시저장할 image"),
                                partWithName("post").description("임시저장할 title과 content를 담아주세요")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("임시저장된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription()),
                                        fieldWithPath("imgUrl").type(JsonFieldType.STRING).description("임시저장된 image url")
                                )
                ));
    }

    @Test
    public void post를_수정하고_임시저장() throws Exception {
        // given
        Long postId = 10L;
        TempPostSaveResponseDto responseDto = new TempPostSaveResponseDto(postId, LocalDateTime.now(), "임시저장된 title", "임시저장된 content");
        TempPostSaveRequestDto requestDto = new TempPostSaveRequestDto("임시저장할 title", "임시저장할 content");
        given(postService.modifyPostAndSaveAsTemp(any(TempPostSaveRequestDto.class), anyLong())).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts/temp/{postId}", postId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-modify-temp-post-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("임시저장할 postId")
                        ),
                        requestFields(
                                fieldWithPath("title").description("임시저장할 title"),
                                fieldWithPath("content").description("임시저장할 content")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("임시저장된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("임시저장된 시간"),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription())
                                )
                ));
    }

    @Test
    public void 임시_포스트을_발행하거나_발행된_포스트를_재발생() throws Exception {
        // given
        Long postId = 20L;
        PostSaveRequestDto requestDto = new PostSaveRequestDto(10L, "post title", "post content");
        PostSaveResponseDto responseDto = new PostSaveResponseDto(10L, postId, "발행된 title", "발행된 content");
        given(postService.publishPost(any(PostSaveRequestDto.class), anyLong())).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v2/posts/{postId}", postId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-re-create-v2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("post Id")
                        ),
                        requestFields(
                                fieldWithPath("userStuffId").description("저장될 UserStuffId"),
                                fieldWithPath("title").description("title"),
                                fieldWithPath("content").description("content")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("발행된 post")))
                                .andWithPrefix("data.",
                                        fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description("post가 저장되는 UserStuffId"),
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription())
                                )
                ));
    }

    @Test
    public void 임시_목록_조회() throws Exception {
        // given
        List<TempPostSaveResponseDto> responseDtoList = new ArrayList<>();
        TempPostSaveResponseDto responseDto1 = new TempPostSaveResponseDto(10L, LocalDateTime.now(), "title1", "content1");
        TempPostSaveResponseDto responseDto2 = new TempPostSaveResponseDto(11L, LocalDateTime.now(), "title2", "content2");
        responseDtoList.add(responseDto1);
        responseDtoList.add(responseDto2);
        given(postService.findTempPosts()).willReturn(responseDtoList);

        mockMvc.perform(get("/api/v2/posts/temp"))
                .andExpect(status().isOk())
                .andDo(document("post-find-temp-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("임시 저장된 post 목록")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("postId").type(POST_ID.getJsonFieldType()).description(POST_ID.getDescription()),
                                        fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("임시저장된 시간"),
                                        fieldWithPath("title").type(POST_TITLE.getJsonFieldType()).description(POST_TITLE.getDescription()),
                                        fieldWithPath("content").type(POST_CONTENT.getJsonFieldType()).description(POST_CONTENT.getDescription())
                                )

                ));
    }

    @Test
    public void guestbook_작성() throws Exception {
        // given
        Long responseId = 20L;
        PostGuestBookRequestDto requestDto = PostGuestBookRequestDto.builder()
                .content("guestbook content").build();
        given(postService.createGuestBookPost(anyString(), any(PostGuestBookRequestDto.class))).willReturn(responseId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts/guestbook/{username}", "infomansion")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("post-create-guestbook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("GuestBook이 존재하는 방의 username")
                        ),
                        requestFields(
                                fieldWithPath("content").description("방명록의 내용")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(POST_ID.getDescription())))
                ));
    }

    @Test
    public void guestbook_수정() throws Exception {
        // given
        Long responseId = 20L;
        PostGuestBookModifyRequestDto requestDto = PostGuestBookModifyRequestDto.builder()
                .postId(responseId)
                .content("guestbook content").build();
        given(postService.modifyGuestBookPost(any(PostGuestBookModifyRequestDto.class))).willReturn(true);

        // when, then
        mockMvc.perform(put("/api/v1/posts/guestbook")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-modify-guestbook",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").description(POST_ID.getDescription()),
                                fieldWithPath("content").description("방명록의 내용")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("수정 성공 여부")))
                ));
    }
}
