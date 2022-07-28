package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Stuff stuff;
    private UserStuff userStuff;
    private String uCategories;

    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        uCategories = "IT,COOK,GAME";

        user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());

        // stuff 생성
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME,DAILY";

        Stuff requestStuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(StuffType.STUFF)
                .geometry("geometry")
                .materials("materials")
                .build();

        stuff = stuffRepository.save(requestStuff);
    }

    @AfterEach
    public void cleanUp(){
        postRepository.deleteAll();
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("User의 관심 카테고리와 일치하는 Post 조회 성공")
    @Test
    public void Post_Category_조회_성공(){
        //given

        // userStuff 생성 및 배치
        userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        LocalDateTime createdTime = LocalDateTime.now();
        BigDecimal PandR = BigDecimal.ONE;
        userStuff.changeIncludedStatus("IT모음집", "IT",
                PandR, PandR, PandR,
                PandR, PandR, PandR);

        userStuffRepository.save(userStuff);

        List<String> CategoriesToString = Arrays.asList(uCategories.split(","));

        List<Category> categories = CategoriesToString.stream()
                .map(Category::valueOf)
                .collect(Collectors.toList());

        //when
        for(int i=0;i<2;i++){
            Post post = Post.builder()
                    .userStuff(userStuff)
                    .title("IT에 대하여" + i)
                    .content("내 포스트는 IT에 관한 것이다. " + i)
                    .build();
//            for(int j=0;j<=i+1;j++){
//                post.postLikes();
//            }
            postRepository.save(post);
        }

        //then
        List<Post> postList = postRepository.findDistinctByUserByCategoryInOrderByLikesDesc(categories);

        assertThat(postList.size()).isEqualTo(2);
        assertThat(postList.get(0).getTitle()).isEqualTo("IT에 대하여1");
    }

    @DisplayName("User의 관심 카테고리와 일치하는 Post가 없는 경우 조회 성공")
    @Test
    public void Post_Category_조회_성공2(){
        //given

        // userStuff 생성 및 배치
        userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        LocalDateTime createdTime = LocalDateTime.now();
        BigDecimal PandR = BigDecimal.ONE;
        userStuff.changeIncludedStatus("컴퓨터는내인생", "DAILY",
                PandR, PandR, PandR,
                PandR, PandR, PandR);

        userStuffRepository.save(userStuff);

        List<String> CategoriesToString = Arrays.asList(uCategories.split(","));

        List<Category> categories = CategoriesToString.stream()
                .map(Category::valueOf)
                .collect(Collectors.toList());

        //when
        for(int i=0;i<2;i++){
            Post post = Post.builder()
                    .userStuff(userStuff)
                    .title("DAILY에 대하여" + i)
                    .content("내 포스트는 DAILY에 관한 것이다. " + i)
                    .build();
            postRepository.save(post);
        }

        //then
        List<Post> postList = postRepository.findDistinctByUserByCategoryInOrderByLikesDesc(categories);

        assertThat(postList.size()).isEqualTo(0);
    }
}