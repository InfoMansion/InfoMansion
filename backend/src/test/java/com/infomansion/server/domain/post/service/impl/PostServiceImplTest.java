package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.dto.PostRequestDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    private Long userId;
    private Long stuffId;
    private Long userStuffId;

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

        // User 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        userId = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build()).getId();

        stuffId = stuffRepository.save(requestDto.toEntity()).getId();

        // UserStuff 생성
        UserStuffRequestDto userStuffRequestDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffId)
                .build();

        userStuffId = userStuffService.saveUserStuff(userStuffRequestDto);

    }

    @AfterEach
    public void cleanUp() {
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

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .userStuffId(userStuffId)
                .title(title).content(content)
                .build();

        Long postId = postService.createPost(postRequestDto);
        assertThat(title).isEqualTo(postRepository.findById(postId).get().getTitle());



    }

}
