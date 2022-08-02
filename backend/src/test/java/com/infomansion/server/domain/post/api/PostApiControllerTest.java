package com.infomansion.server.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
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

import static com.infomansion.server.domain.user.domain.User.builder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private Long userId;
    private Long stuffId;
    private Long userStuffId;
    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String uCategories = "IT,COOK";

        userId = userRepository.save(builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build()).getId();

        // stuff 생성
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME,DAILY";
        String stuffType = "STUFF";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .build();

        stuffId = stuffRepository.save(requestDto.toEntity()).getId();

        // UserStuff 생성, 배치
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);
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

    @DisplayName("post 추천에 성공했습니다.")
    @WithCustomUserDetails
    @Test
    public void Post_추천_성공() throws Exception{
        // 다른 User 생성
        String email = "infomansion@test.com1";
        String password = "testPassword1$1";
        String tel = "010123456781";
        String username = "infomansion1";
        String uCategories = "IT,COOK";

        User PostCreateUser = userRepository.save(builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());
        Long PostCreateUserId = PostCreateUser.getId();

        // 다른 User의 UserStuff 생성 및 배치
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(PostCreateUserId)
                .stuffId(stuffId).build();

        userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java란 뭘까?").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        userStuffId = userStuffService.includeUserStuff(includeDto);

        //post 작성

        Post post = Post.builder().user(PostCreateUser).userStuff(userStuffRepository.findById(userStuffId).get())
                        .title("EffectiveJava").content("자바개발자 필독서").build();
        postRepository.saveAndFlush(post);

        mockMvc.perform(get("/api/v1/posts/recommend"))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
