package com.infomansion.server.domain.userstuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserStuffRepositoryTest {

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

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
                .stuffType(StuffType.STUFF)
                .geometry("geometry")
                .materials("materials")
                .build();

        stuff = stuffRepository.save(requestStuff);
    }

    @AfterEach
    public void cleanUp() {
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("UserStuff 생성시간 조회")
    @Test
    public void userstuff_생성시간_조회() {
        // when
        UserStuff userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        LocalDateTime createdTime = LocalDateTime.now();
        Long userStuffId = userStuffRepository.save(userStuff).getId();

        // then
        Optional<UserStuff> result = userStuffRepository.findById(userStuffId);
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getCreatedDate()).isAfterOrEqualTo(createdTime);
    }

    @DisplayName("UserStuff가 수정될 경우에도 생성시간 변화 없음")
    @Test
    public void userstuff_생성시간_조회_2() {
        // given
        UserStuff userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        UserStuff savedUserStuff = userStuffRepository.save(userStuff);

        // when
        LocalDateTime modifiedTime = LocalDateTime.now();
        savedUserStuff.changeAliasAndCategory("test", "IT");
        userStuffRepository.flush();

        // then
        Optional<UserStuff> result = userStuffRepository.findById(savedUserStuff.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getCreatedDate()).isBeforeOrEqualTo(modifiedTime);
    }

    @DisplayName("UserStuff 수정시간 조회")
    @Test
    public void userstuff_수정시간_조회() {
        // given
        UserStuff userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        UserStuff savedUserStuff = userStuffRepository.save(userStuff);

        // when
        LocalDateTime modifiedTime = LocalDateTime.now();
        savedUserStuff.changeAliasAndCategory("test", "IT");
        userStuffRepository.flush();

        // then
        Optional<UserStuff> result = userStuffRepository.findById(savedUserStuff.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getModifiedDate()).isAfterOrEqualTo(modifiedTime);
    }

    @DisplayName("UserStuff 삭제시간 조회")
    @Test
    public void userstuff_삭제시간_조회() {
        // given
        UserStuff userStuff = UserStuff.builder()
                .user(user)
                .stuff(stuff).build();
        UserStuff savedUserStuff = userStuffRepository.save(userStuff);

        // when
        LocalDateTime deletedDate = LocalDateTime.now();
        savedUserStuff.setDeletedDate();
        userStuffRepository.flush();

        // then
        Optional<UserStuff> result = userStuffRepository.findById(savedUserStuff.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getDeletedDate()).isAfterOrEqualTo(deletedDate);
    }
}
