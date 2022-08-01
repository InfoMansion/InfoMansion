package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;
import com.infomansion.server.domain.post.repository.LikesPostRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostServiceImplTest {

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private LikesPostRepository likesPostRepository;

    private Long userId, targetUserId;
    private Long stuffId;
    private Long userStuffId;
    private UserStuff userStuff;
    private User user;
    private Stuff stuff;

    @BeforeEach
    public void setUp() {

        // Stuff 생성
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String sCategories = "IT,GAME";
        String stuffType = "STUFF";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(sCategories)
                .stuffType(stuffType)
                .geometry("geometry")
                .materials("materials")
                .build();
        stuffId = stuffRepository.save(requestDto.toEntity()).getId();

        // User  생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build());

        userId = user.getId();

        // UserStuff 생성
        UserStuffRequestDto userStuffRequestDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffId)
                .build();
        userStuffId = userStuffService.saveUserStuff(userStuffRequestDto);

        // UserStuff 배치
        UserStuffIncludeRequestDto userIncludeRequestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT")
                .alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffId = userStuffService.includeUserStuff(userIncludeRequestDto);

    }

    @AfterEach
    public void cleanUp() {
        likesPostRepository.deleteAll();
        postRepository.deleteAll();
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("User가 가진 userStuff에 Post 생성 성공")
    @Test
    public void post_생성_및_조회(){

        //given
        String title = "notebook의 종류";
        String content = "사실 notebook이 아니라 labtop이 바른 말이다.";

        PostCreateRequestDto postCreateRequestDto = PostCreateRequestDto.builder()
                .userId(userId).userStuffId(userStuffId)
                .title(title).content(content)
                .build();

        Long postId = postService.createPost(postCreateRequestDto);
        assertThat(title).isEqualTo(postRepository.findById(postId).get().getTitle());
    }

    @DisplayName("Post 추천을 통해 1명의 User만 추천된다.")
    @Test
    public void post_생성_및_조회2(){
        // given
        for(int i=0;i<4;i++){
            String title = "Post" + i;
            String content = "comment";

            PostCreateRequestDto postCreateRequestDto = PostCreateRequestDto.builder()
                    .userId(userId).userStuffId(userStuffId)
                    .title(title).content(content)
                    .build();
            Long postId = postService.createPost(postCreateRequestDto);
            for(int j=0;j<i+1;j++){
                likesPostRepository.findById(postId).get().addPostLikes();
            }
        }

        // Target User  생성
        String email = "infomansion@test.com2";
        String password = "testPassword1$2";
        String tel = "010123456782";
        String username = "infomansion2";
        String categories = "IT,SPORTS";

        userId = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build()).getId();

        List<PostRecommendResponseDto> RecommendPostList = postService.findRecommendPost(userId);
        assertThat(RecommendPostList.size()).isEqualTo(1);

    }

    @DisplayName("Post 추천 시 자신의 Post는 추천하지 않는다.")
    @Test
    public void post_생성_및_조회3(){

        // given
        String title0 = "Post";
        String content0 = "comment";

        PostCreateRequestDto postCreateRequestDto = PostCreateRequestDto.builder()
                .userId(userId).userStuffId(userStuffId)
                .title(title0).content(content0)
                .build();
        Long postId0 = postService.createPost(postCreateRequestDto);
        Post post0 = postRepository.findById(postId0).get();
        likesPostRepository.findById(postId0).get().addPostLikes();

        // Target User  생성
        String email = "infomansion@test.com2";
        String password = "testPassword1$2";
        String tel = "010123456782";
        String username = "infomansion2";
        String categories = "IT,SPORTS";

        userId = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build()).getId();

        // Target User의 UserStuff 생성 및 배치
        UserStuffRequestDto userStuffRequestDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffId)
                .build();
        userStuffId = userStuffService.saveUserStuff(userStuffRequestDto);

        UserStuffIncludeRequestDto userIncludeRequestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT")
                .alias("Python의세계")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();

        userStuffId = userStuffService.includeUserStuff(userIncludeRequestDto);

        // TargetUser의 Post생성

        String title = "TargetPost";
        String content = "comment123";

        postCreateRequestDto = PostCreateRequestDto.builder()
                .userId(userId).userStuffId(userStuffId)
                .title(title).content(content)
                .build();

        Long postId = postService.createPost(postCreateRequestDto);
        for(int i=0;i<3;i++){
            likesPostRepository.findById(postId).get().addPostLikes();
        }

        List<PostRecommendResponseDto> RecommendPostList = postService.findRecommendPost(userId);
        assertThat(RecommendPostList.get(0)).isNotEqualTo(new PostRecommendResponseDto(post0));
    }
}
