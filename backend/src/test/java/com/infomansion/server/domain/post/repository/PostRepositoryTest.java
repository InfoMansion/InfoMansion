package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Stuff stuff;

    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String uCategories = "IT,COOK";

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
        String categories = "IT,GAME";

        Stuff requestStuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(StuffType.OTHER)
                .geometry("geometry")
                .material("materials")
                .build();

        stuff = stuffRepository.save(requestStuff);
    }

    @AfterEach
    public void cleanUp() {
        postRepository.deleteAll();
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("공개 권한이 private인 User의 post는 검색되지 않는다.")
    @WithCustomUserDetails
    @Test
    public void Post_By_User_공개권한() throws Exception{

        Long stuffId = stuff.getId();
        Long userId = user.getId();

        // UserStuff 생성
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffId).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        //UserStuff 배치
        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        List<UserStuffEditRequestDto> includeDtoList = new ArrayList<>();
        includeDtoList.add(includeDto);
        userStuffService.editUserStuff(includeDtoList);

        //post 작성
        for(int i=0;i<7;i++){
            Post post = Post.builder().user(user).userStuff(userStuffRepository.findById(userStuffId).get())
                    .title("EffectiveJava ver." + (i+1)).content("자바개발자 필독서 ver."+ (i+1)).build();
            postRepository.save(post);
        }
        Pageable pageable = PageRequest.of(0, 5);
        Slice<Post> postListPublic = postRepository.findByTitle("EffectiveJava", pageable);

        assertThat(postListPublic.getContent().size()).isEqualTo(5);

        user.changePrivate();

        Slice<Post> postListPrivate =  postRepository.findByTitle("EffectiveJava", pageable);
        assertThat(postListPrivate.getContent().size()).isEqualTo(0);

    }
}