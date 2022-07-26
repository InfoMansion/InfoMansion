package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("User 생성시간 조회 성공")
    @Test
    public void User_생성시간_조회() {
        User user = User.builder()
                .email("infomansion@test.com")
                .password("Testtest11!!!")
                .username("인포멘션 테스트 계정")
                .tel("010-1234-5678")
                .categories("IT")
                .build();

        LocalDateTime createdTime = LocalDateTime.now();
        Long userId = userRepository.save(user).getId();

        Optional<User> result = userRepository.findById(userId);
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getCreatedDate()).isAfterOrEqualTo(createdTime);
    }

    @DisplayName("User가 수정될 경우에도 생성시간 변화 없음")
    @Test
    public void User_생성시간_조회_2() {
        User user = User.builder()
                .email("infomansion@test.com")
                .password("Testtest11!!!")
                .username("인포멘션 테스트 계정")
                .tel("010-1234-5678")
                .categories("IT")
                .build();
        User savedUser = userRepository.save(user);

        LocalDateTime modifiedTime = LocalDateTime.now();
        savedUser.changeCategories("MUSIC,ART");
        userRepository.flush();

        Optional<User> result = userRepository.findById(savedUser.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getCreatedDate()).isBefore(modifiedTime);
    }

    @DisplayName("User 수정시간 조회 성공")
    @Test
    public void User_수정시간_조회() {
        User user = User.builder()
                .email("infomansion@test.com")
                .password("Testtest11!!!")
                .username("인포멘션 테스트 계정")
                .tel("010-1234-5678")
                .categories("IT")
                .build();
        User savedUser = userRepository.save(user);

        LocalDateTime modifiedTime = LocalDateTime.now();
        savedUser.changeCategories("MUSIC,ART");
        userRepository.flush();

        Optional<User> result = userRepository.findById(savedUser.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getModifiedDate()).isAfterOrEqualTo(modifiedTime);
    }

    @DisplayName("User 삭제시간 조회 성공")
    @Test
    public void User_삭제시간_조회() {
        User user = User.builder()
                .email("infomansion@test.com")
                .password("Testtest11!!!")
                .username("인포멘션 테스트 계정")
                .tel("010-1234-5678")
                .categories("IT")
                .build();
        User savedUser = userRepository.save(user);

        LocalDateTime deletedDate = LocalDateTime.now();
        savedUser.setDeletedDate();
        userRepository.flush();

        Optional<User> result = userRepository.findById(savedUser.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getDeletedDate()).isAfterOrEqualTo(deletedDate);
    }
}
