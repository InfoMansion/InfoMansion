package com.infomansion.server.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffSaveRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import static com.infomansion.server.domain.user.domain.User.builder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class PostApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private LikesPostService likesPostService;

    private Long userId;
    private Long stuffId;
    private Long userStuffId;
    private User user;
    @BeforeEach
    @WithCustomUserDetails
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String uCategories = "IT,COOK";

        user = userRepository.save(builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());
        userId = user.getId();

        // stuff 생성
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME,DAILY";
        String stuffType = "DESK";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .build();

        stuffId = stuffRepository.save(requestDto.toEntity()).getId();


    }

    @AfterEach
    public void clearUp(){
        postRepository.deleteAll();
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("post 생성에 성공했습니다.")
    @WithCustomUserDetails
    @Test
    public void post_생성_성공() throws Exception{
        // UserStuff 생성, 배치
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        PostCreateRequestDto postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("EffectiveJava")
                .content("자바개발자 필독서")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String createForm = objectMapper.writeValueAsString(postCreateDto);
        mockMvc.perform(post("/api/v1/posts")
                        .content(createForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("UserStuff 안에있는 Post를 모두 반환한다.")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void post_UserStuff로_검색() throws Exception{
        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        for(int i=0;i<3;i++){
            Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                    .title("EffectiveJava ver." + (i+1)).content("자바개발자 필독서 ver."+ (i+1)).build();
            postRepository.saveAndFlush(post);
            for(int j=0;j<i+1;j++) post.getLikesPost().addPostLikes();
        }

        mockMvc.perform(get("/api/v1/posts/"+userStuffId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data[0].['title']").value("EffectiveJava ver.1"));
    }

    @DisplayName("좋아요 버튼을 누르면 Post의 Likes가 증가한다.")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_좋아요_성공() throws Exception{

        // UserStuff 생성, 배치
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        PostCreateRequestDto postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("EffectiveJava")
                .content("자바개발자 필독서")
                .build();

        Long postId = postService.createPost(postCreateDto);


        mockMvc.perform(put("/api/v1/posts/likes/"+postId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/posts/"+userStuffId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data[0].['likes']").value(1));

    }

    @DisplayName("Post 검색에 성공하였습니다.")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_검색_성공() throws Exception{

        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        PostCreateRequestDto postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("EffectiveJava자바 infomansion")
                .content("infomansion Java 파이팅!")
                .build();

        postService.createPost(postCreateDto);

        postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("킹갓infomansion")
                .content("Java개발자 infomansion 필독서")
                .build();

        postService.createPost(postCreateDto);
        String searchWord = "infomansion";
        int page = 0;
        int size = 3;

        mockMvc.perform(get("/api/v1/users/search/username"+"?searchWord="+searchWord+"&page"+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.['usersByUserName'].['content'].size()").value(1));

        mockMvc.perform(get("/api/v1/posts/search/title"+"?searchWord="+searchWord+"&page"+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.['postsByTitleOrContent'].['content'][0].['title']").value("EffectiveJava자바 infomansion"));

        mockMvc.perform(get("/api/v1/posts/search/content"+"?searchWord="+searchWord+"&page"+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.['postsByTitleOrContent'].['content'][1].['content']").value("Java개발자 infomansion 필독서"));

    }

    @DisplayName("대표 이미지 없는 Post 상세 조회 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_상세조회_성공() throws Exception{

        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        PostCreateRequestDto postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("EffectiveJava")
                .content("자바개발자 필독서")
                .build();

        Long postId = postService.createPost(postCreateDto);

        for(int i=0;i<3;i++) likesPostService.addLikes(postId);

        mockMvc.perform(get("/api/v1/posts/detail/"+postId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.['userName']").value("infomansion"))
                .andExpect((ResultMatcher) jsonPath("$.data.['title']").value("EffectiveJava"))
                .andExpect((ResultMatcher) jsonPath("$.data.['content']").value("자바개발자 필독서"))
                .andExpect((ResultMatcher) jsonPath("$.data.['category']").value("IT"))
                .andExpect((ResultMatcher) jsonPath("$.data.['likes']").value(3))
                .andExpect((ResultMatcher) jsonPath("$.data.['defaultPostThumbnail']").value("https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/thumbnail/IT_defaultThumbnail.jpeg"));


    }

    @DisplayName("대표 이미지 있는 Post 상세 조회 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_상세조회_성공2() throws Exception{

        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성
        PostCreateRequestDto postCreateDto = PostCreateRequestDto.builder()
                .userStuffId(userStuffId)
                .title("EffectiveJava")
                .content("자바개발자 필독서, <img src=\"https://cdn.pixabay.com/photo/2021/08/25/07/21/cat-6572630_1280.jpg\"> ")
                .build();

        Long postId = postService.createPost(postCreateDto);

        for(int i=0;i<3;i++) likesPostService.addLikes(postId);

        mockMvc.perform(get("/api/v1/posts/detail/"+postId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.['userName']").value("infomansion"))
                .andExpect((ResultMatcher) jsonPath("$.data.['title']").value("EffectiveJava"))
                .andExpect((ResultMatcher) jsonPath("$.data.['content']").value("자바개발자 필독서, <img src=\"https://cdn.pixabay.com/photo/2021/08/25/07/21/cat-6572630_1280.jpg\"> "))
                .andExpect((ResultMatcher) jsonPath("$.data.['category']").value("IT"))
                .andExpect((ResultMatcher) jsonPath("$.data.['likes']").value(3))
                .andExpect((ResultMatcher) jsonPath("$.data.['defaultPostThumbnail']").value("https://cdn.pixabay.com/photo/2021/08/25/07/21/cat-6572630_1280.jpg"));


    }

    @DisplayName("User의 가장 최근 Post를 5개 반환한다.")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void post_User_recent5() throws Exception{
        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);
        String userName = "infomansion";
        //post 작성
        for(int i=0;i<7;i++){
            Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                    .title("EffectiveJava ver." + (10-i)).content("자바개발자 필독서 ver."+ (10-i)).build();
            postRepository.saveAndFlush(post);
            for(int j=0;j<i+1;j++) post.getLikesPost().addPostLikes();
        }

        mockMvc.perform(get("/api/v1/posts/recent?userName="+userName))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data.size()").value(5))
                .andExpect((ResultMatcher) jsonPath("$.data[0].['content']").value("자바개발자 필독서 ver.10"));

    }

}
