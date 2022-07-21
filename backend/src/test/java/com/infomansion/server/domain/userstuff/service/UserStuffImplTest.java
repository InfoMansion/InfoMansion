package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStuffImplTest {

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId;
    private List<Long> stuffIds;

    @BeforeEach
    public void setUp() {
        // user 생성
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

        // stuff 생성
        stuffIds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "노트북"+(i+1);
            Long price = 30L;
            String category = "IT";
            String stuffType = "STUFF";

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .category(category)
                    .stuffType(stuffType)
                    .build();

            stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
        }
    }

    @AfterEach
    public void cleanUp() {
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();;
    }

    @DisplayName("사용자가 보유하는 Stuff 목록에 새로운 Stuff를 추가한 뒤 조회")
    @Test
    public void user_stuff_생성_및_조회() {
        // given

        // when
        UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                .stuffId(stuffIds.get(0)).userId(userId).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDto);

        // then
        UserStuffResponseDto result = userStuffService.findUserStuffByUserStuffId(userStuffId);
        assertThat(result.getStuffNameKor()).isEqualTo("노트북1");
    }

    @DisplayName("사용자가 보유하고 있는 모든 Stuff 조회")
    @Test
    public void user_stuff_전체_조회() {
        // given

        // when
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                    .stuffId(stuffIds.get(i)).userId(userId).build();
            userStuffService.saveUserStuff(requestDto);
        }

        // then
        List<UserStuffResponseDto> list = userStuffService.findAllUserStuff(userId);
        assertThat(list.size()).isEqualTo(5);
    }
}
