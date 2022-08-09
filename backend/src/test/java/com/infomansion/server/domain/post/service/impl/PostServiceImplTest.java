package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffEditRequestDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.domain.user.domain.User.builder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class PostServiceImplTest {

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

    @DisplayName("UserStuff의 카테고리 변경 시 Post의 카테고리도 변경 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void After_UserStuff_Modifying() throws Exception{
        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        List<UserStuffEditRequestDto> includeDtoList = new ArrayList<>();
        includeDtoList.add(includeDto);

        userStuffService.editUserStuff(includeDtoList);
        String userName = "infomansion";
        //post 작성
        for(int i=0;i<7;i++){
            Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                    .title("EffectiveJava ver." + (10-i)).content("자바개발자 필독서 ver."+ (10-i)).build();
            System.out.println("post = " + post.getCategory());
            postRepository.saveAndFlush(post);
        }

        // userStuff의 카테고리 변경
        UserStuffEditRequestDto modifiedIncludeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("데일리").category("DAILY")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        includeDtoList = new ArrayList<>();
        includeDtoList.add(modifiedIncludeDto);
        userStuffService.editUserStuff(includeDtoList);

        List<PostSimpleResponseDto> response = postService.findPostByUserStuffId(userStuffId);
        for (PostSimpleResponseDto postSimpleResponseDto : response) {
            assertThat(postSimpleResponseDto.getCategory().getCategory()).isEqualTo("DAILY");
        }

    }

}
