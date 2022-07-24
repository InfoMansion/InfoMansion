package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffModifyRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("사용자가 보유하고 있는 Stuff 하나를 배치된 상태로 변경")
    @Test
    public void user_stuff_배치_상태로_변경() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                    .stuffId(stuffIds.get(i)).userId(userId).build();
            userStuffId = userStuffService.saveUserStuff(requestDto);
        }

        // when
        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT")
                .alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffId = userStuffService.includeUserStuff(requestDto);

        // then
        Optional<UserStuff> findUS = userStuffRepository.findById(userStuffId);
        assertThat(findUS.isEmpty()).isFalse();
        assertThat(findUS.get().getSelected()).isTrue();
        assertThat(findUS.get().getAlias()).isEqualTo("Java 모음집");
    }

    @DisplayName("이미 배치된 Stuff를 다시 배치된 상태로 변경할 경우 오류")
    @Test
    public void user_stuff_배치_상태로_변경_오류() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                    .stuffId(stuffIds.get(i)).userId(userId).build();
            userStuffId = userStuffService.saveUserStuff(requestDto);
        }

        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT")
                .alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffId = userStuffService.includeUserStuff(requestDto);

        // when
        UserStuffIncludeRequestDto usird = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("GAME")
                .alias("게임 모음")
                .posX(2.1).posY(3.5).posZ(0.9)
                .rotX(0.8).rotY(1.2).rotZ(1.0)
                .build();

        // then
        assertThatThrownBy(() -> {userStuffService.includeUserStuff(usird);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(40062);
    }

    @DisplayName("사용자가 보유하고 있는 Stuff 하나를 제외된 상태로 변경")
    @Test
    public void user_stuff_제외_상태로_변경() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                    .stuffId(stuffIds.get(i)).userId(userId).build();
            userStuffId = userStuffService.saveUserStuff(requestDto);
        }
        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT")
                .alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffId = userStuffService.includeUserStuff(requestDto);

        // when
        userStuffService.excludeUserStuff(userStuffId);

        // then
        Optional<UserStuff> findUS = userStuffRepository.findById(userStuffId);
        assertThat(findUS.isEmpty()).isFalse();
        assertThat(findUS.get().getSelected()).isFalse();
        assertThat(findUS.get().getAlias()).isEqualTo("Java 모음집");
    }

    @DisplayName("이미 제외된 Stuff를 다시 제외된 상태로 변경 요청할 경우 오류")
    @Test
    public void user_stuff_제외_상태로_변경_오류() {
        // given
        UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                .stuffId(stuffIds.get(0)).userId(userId).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDto);

        // when, then
        assertThatThrownBy(() -> {userStuffService.excludeUserStuff(userStuffId);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(40061);
    }

    @DisplayName("배치된 Stuff의 Alias나 Category 변경")
    @Test
    public void user_stuff_alias_category_변경() {
        // given
        UserStuffRequestDto requestDtoGiven = UserStuffRequestDto.builder()
                .stuffId(stuffIds.get(0)).userId(userId).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDtoGiven);

        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT").alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffService.includeUserStuff(requestDto);

        // when
        UserStuffModifyRequestDto modifyRequestDto = UserStuffModifyRequestDto.builder()
                .id(userStuffId)
                .category("GAME")
                .alias("메이플 공략 모음")
                .build();

        userStuffService.modifyAliasAndCategory(modifyRequestDto);

        // then
        Optional<UserStuff> findUS = userStuffRepository.findById(userStuffId);
        assertThat(findUS.isEmpty()).isFalse();
        assertThat(findUS.get().getCategory()).isEqualTo(Category.GAME);
        assertThat(findUS.get().getAlias()).isEqualTo("메이플 공략 모음");
    }

    @DisplayName("Alias, Category 수정 시 둘 다 미입력할 경우 오류 발생")
    @Test
    public void user_stuff_alias_category_변경_오류() {
        // given
        UserStuffRequestDto requestDtoGiven = UserStuffRequestDto.builder()
                .stuffId(stuffIds.get(0)).userId(userId).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDtoGiven);

        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .category("IT").alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0)
                .rotX(0.0).rotY(0.2).rotZ(2.2)
                .build();
        userStuffService.includeUserStuff(requestDto);

        // when
        UserStuffModifyRequestDto modifyRequestDto = UserStuffModifyRequestDto.builder()
                .id(userStuffId)
                .build();

        // then
        assertThatThrownBy(() -> {userStuffService.modifyAliasAndCategory(modifyRequestDto);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(40063);

    }

    @DisplayName("userStuffId로 삭제 성공")
    @Test
    public void user_stuff_삭제_성공() {
        // given
        UserStuffRequestDto requestDtoGiven = UserStuffRequestDto.builder()
                .stuffId(stuffIds.get(0)).userId(userId).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDtoGiven);

        // when
        userStuffService.removeUserStuff(userStuffId);

        // then
        assertThatThrownBy(() -> {userStuffService.findUserStuffByUserStuffId(userStuffId);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(ErrorCode.USER_STUFF_NOT_FOUND.getCode());

    }

}
