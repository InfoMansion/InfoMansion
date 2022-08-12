package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostModifyRequestDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.dto.PostbyUserStuffResponseDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffEditRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffSaveRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.infomansion.server.domain.user.domain.User.builder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
    private EntityManager em;

    private Long userId;
    private Long stuffId;
    private Long userStuffId;
    private User user;

    @BeforeEach
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
                .userStuffId(userStuffId).alias("Java 정리").selectedCategory("IT")
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
            postRepository.saveAndFlush(post);
        }

        // userStuff의 카테고리 변경
        UserStuffEditRequestDto modifiedIncludeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("데일리").selectedCategory("DAILY")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        includeDtoList = new ArrayList<>();
        includeDtoList.add(modifiedIncludeDto);
        userStuffService.editUserStuff(includeDtoList);

        Pageable pageable = PageRequest.of(0, 5);
        PostbyUserStuffResponseDto response = postService.findPostByUserStuffId(userStuffId, pageable);
        for (PostSimpleResponseDto postSimpleResponseDto : response.getPostsByUserStuff().getContent()) {
            assertThat(postSimpleResponseDto.getCategory().getCategory()).isEqualTo("DAILY");
        }
    }

    @DisplayName("Post 삭제 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_삭제_성공() {
        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").selectedCategory("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        List<UserStuffEditRequestDto> includeDtoList = new ArrayList<>();
        includeDtoList.add(includeDto);
        userStuffService.editUserStuff(includeDtoList);

        // Post 생성
        Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                .title("EffectiveJava").content("자바개발자 필독서").build();
        Post savedPost = postRepository.save(post);
        postService.deletePost(savedPost.getId());
        em.flush();
        em.clear();


        Optional<Post> result = postRepository.findById(savedPost.getId());
        assertThat(result).isEmpty();
    }

    @DisplayName("Post 작성자가 아닐 경우 삭제 실패")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void Post_삭제_실패() {
        // user 생성
        String email = "infomansion2@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion2";
        String uCategories = "IT,COOK";

        user = userRepository.save(builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());

        // UserStuff 생성 및 배치
        UserStuff userStuff = UserStuff.builder()
                .user(user).stuff(stuffRepository.findById(stuffId).get())
                .selected(true).category(Category.IT).alias("Java 정리").build();
        userStuff = userStuffRepository.save(userStuff);

        // Post 생성
        Post post = Post.builder()
                .user(user).userStuff(userStuff)
                .title("EffectiveJava").content("자바개발자 필독서").build();
        Post savedPost = postRepository.save(post);

        assertThatThrownBy(() ->  {postService.deletePost(savedPost.getId()); })
                .isInstanceOf(CustomException.class)
                        .extracting("errorCode").isEqualTo(ErrorCode.USER_NO_PERMISSION);
    }

    @DisplayName("로그인한 사용자는 자신의 게시판에 누가 작성했더라도 삭제 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void 사용자의_UserStuff에_작성된_어떠한_Post라도_삭제_성공() {
        // user 생성
        String email = "infomansion2@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion2";
        String uCategories = "IT,COOK";

        user = userRepository.save(builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());

        // UserStuff 생성 및 배치
        UserStuff userStuff = UserStuff.builder()
                .user(userRepository.findById(userId).get()).stuff(stuffRepository.findById(stuffId).get())
                .selected(true).category(Category.IT).alias("Java 정리").build();
        userStuff = userStuffRepository.save(userStuff);

        // Post 생성
        Post post = Post.builder()
                .user(user).userStuff(userStuff)
                .title("EffectiveJava").content("자바개발자 필독서").build();
        Post savedPost = postRepository.save(post);

        postService.deletePost(savedPost.getId());
        em.flush();
        em.clear();

        Optional<Post> result = postRepository.findById(savedPost.getId());
        assertThat(result).isEmpty();
    }

    @DisplayName("Post의 title, content 수정 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void modify_post_with_title_and_content() throws Exception{
        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").selectedCategory("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        List<UserStuffEditRequestDto> includeDtoList = new ArrayList<>();
        includeDtoList.add(includeDto);
        userStuffService.editUserStuff(includeDtoList);

        String userName = "infomansion";
        Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                .title("EffectiveJava").content("자바개발자 필독서").build();
        postRepository.saveAndFlush(post);

        PostModifyRequestDto requestDto = PostModifyRequestDto.builder()
                .userStuffId(userStuffId).postId(post.getId())
                .title("EffectiveJava ver.1").content("자바개발자 필독서 ver.1").images(new ArrayList<>())
                .build();
        postService.modifyPostAndSaveAsTemp(requestDto);

        Optional<Post> findPost = postRepository.findById(post.getId());
        assertThat(findPost).isNotEmpty();
        assertThat(findPost.get().getTitle()).isEqualTo("EffectiveJava ver.1");
        assertThat(findPost.get().getContent()).isEqualTo("자바개발자 필독서 ver.1");
        assertThat(findPost.get().getUserStuff().getId()).isEqualTo(userStuffId);
    }

    @DisplayName("Post의 userStuff 수정 시 카테고리 자동으로 수정 됨")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void modify_post_with_userStuff() throws Exception{
        // 새로운 Stuff 생성
        Stuff stuff = Stuff.builder()
                .categories("NATURE").stuffName("plant").stuffNameKor("화분")
                .stuffType(StuffType.PLANT).build();
        Stuff stuff2 = stuffRepository.save(stuff);

        UserStuff userStuff = UserStuff.builder()
                .user(user).stuff(stuffRepository.findById(stuffId).get())
                .selected(true).alias("Java 모음집").category(Category.IT)
                .posX(BigDecimal.ZERO).posY(BigDecimal.ONE).posZ(BigDecimal.ZERO)
                .rotX(BigDecimal.ONE).rotY(BigDecimal.ONE).rotZ(BigDecimal.ZERO)
                .build();
        userStuffId = userStuffRepository.save(userStuff).getId();

        UserStuff userStuff1 = UserStuff.builder()
                .user(user).stuff(stuff2)
                .selected(true).alias("자연~").category(Category.NATURE)
                .posX(BigDecimal.ZERO).posY(BigDecimal.ONE).posZ(BigDecimal.ZERO)
                .rotX(BigDecimal.ONE).rotY(BigDecimal.ONE).rotZ(BigDecimal.ZERO)
                .build();
        userStuff1 = userStuffRepository.save(userStuff1);

        String userName = "infomansion";
        Post post = Post.builder().user(user).userStuff(userStuff1)
                .title("EffectiveJava").content("자바개발자 필독서").build();
        postRepository.saveAndFlush(post);

        PostModifyRequestDto requestDto = PostModifyRequestDto.builder()
                .userStuffId(userStuffId).postId(post.getId())
                .title("EffectiveJava").content("자바개발자 필독서").images(new ArrayList<>())
                .build();
        postService.modifyPostAndSaveAsTemp(requestDto);

        Optional<Post> findPost = postRepository.findById(post.getId());
        assertThat(findPost).isNotEmpty();
        assertThat(findPost.get().getTitle()).isEqualTo("EffectiveJava");
        assertThat(findPost.get().getContent()).isEqualTo("자바개발자 필독서");
        assertThat(findPost.get().getUserStuff().getId()).isEqualTo(userStuffId);
        assertThat(findPost.get().getCategory()).isEqualTo(Category.IT);
    }

}
