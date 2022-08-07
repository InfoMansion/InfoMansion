package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.payment.domain.Payment;
import com.infomansion.server.domain.payment.domain.PaymentLine;
import com.infomansion.server.domain.payment.repository.PaymentLineRepository;
import com.infomansion.server.domain.payment.repository.PaymentRepository;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserCreditRepository;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStuffServiceImplTest {

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private UserStuffRepository userStuffRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Autowired
    private PaymentLineRepository paymentLineRepository;

    private Long userId;
    private String username;
    private List<Long> stuffIds;
    private long credit = 100000000L;
    private long totalPrice = 0;

    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        username = "infomansion";
        String userCategories = "IT,COOK";

        User user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(userCategories)
                .build());

        userId = user.getId();

        user.getUserCredit().earnCredit(credit);

        userCreditRepository.save(user.getUserCredit());

        // stuff 생성
        totalPrice = 0;
        stuffIds = new ArrayList<>();
        List<String> stuffTypes = Arrays.asList("DESK", "CLOSET", "DRAWER", "WALL", "FLOOR");
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "노트북"+(i+1);
            Long price = 30L;
            String sCategories = "IT,GAME,SPORTS,DAILY,INTERIOR,NONE";
            String stuffType = stuffTypes.get(i % stuffTypes.size());

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .categories(sCategories)
                    .stuffType(stuffType)
                    .geometry("geometry")
                    .material("materials")
                    .build();

            stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());

            totalPrice += price;
        }
    }

    @AfterEach
    public void cleanUp() {
        userStuffRepository.deleteAll();
        paymentRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();;
    }

    @DisplayName("사용자가 보유하는 Stuff 목록에 새로운 Stuff를 추가한 뒤 조회")
    @WithCustomUserDetails
    @Test
    public void user_stuff_생성_및_조회_성공() {
        // given

        // when
        UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDto);

        // then
        UserStuffResponseDto result = userStuffService.findUserStuffByUserStuffId(userStuffId);
        assertThat(result.getStuffNameKor()).isEqualTo("노트북1");
    }

    @DisplayName("사용자가 보유하고 있는 모든 Stuff 조회")
    @WithCustomUserDetails
    @Test
    public void user_stuff_전체_조회_성공() {
        // given

        // when
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
            userStuffService.saveUserStuff(requestDto);
        }

        // then
        List<UserStuffResponseDto> list = userStuffService.findAllUserStuff();
        assertThat(list.size()).isEqualTo(5);
    }

    @DisplayName("사용자가 보유하고 있는 Stuff 하나를 배치된 상태로 변경")
    @WithCustomUserDetails
    @Test
    public void user_stuff_배치_상태로_변경() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
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
    @WithCustomUserDetails
    @Test
    public void user_stuff_배치_상태로_변경_오류() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
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
    @WithCustomUserDetails
    @Test
    public void user_stuff_제외_상태로_변경() {
        // given
        Long userStuffId = 12L;
        for(int i = 0; i < stuffIds.size(); i+=2) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
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
    @WithCustomUserDetails
    @Test
    public void user_stuff_제외_상태로_변경_오류() {
        // given
        UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDto);

        // when, then
        assertThatThrownBy(() -> {userStuffService.excludeUserStuff(userStuffId);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(40061);
    }

    @DisplayName("배치된 Stuff의 Alias나 Category 변경 성공")
    @WithCustomUserDetails
    @Test
    public void user_stuff_alias_category_변경_성공() {
        // given
        UserStuffSaveRequestDto requestDtoGiven = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
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

        userStuffService.modifyAliasOrCategory(modifyRequestDto);

        // then
        Optional<UserStuff> findUS = userStuffRepository.findById(userStuffId);
        assertThat(findUS.isEmpty()).isFalse();
        assertThat(findUS.get().getCategory()).isEqualTo(Category.GAME);
        assertThat(findUS.get().getAlias()).isEqualTo("메이플 공략 모음");
    }

    @DisplayName("Alias, Category 수정 시 둘 다 미입력할 경우 오류 발생")
    @WithCustomUserDetails
    @Test
    public void user_stuff_alias_category_변경_오류() {
        // given
        UserStuffSaveRequestDto requestDtoGiven = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
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
        assertThatThrownBy(() -> {userStuffService.modifyAliasOrCategory(modifyRequestDto);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").extracting("code").isEqualTo(40063);

    }

    @DisplayName("userStuffId로 삭제 성공")
    @WithCustomUserDetails
    @Test
    public void user_stuff_삭제_성공() {
        // given
        UserStuffSaveRequestDto requestDtoGiven = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDtoGiven);

        // when
        userStuffService.removeUserStuff(userStuffId);

        // then
        Optional<UserStuff> removedUserStuff = userStuffRepository.findById(userStuffId);
        assertThat(removedUserStuff.isEmpty()).isTrue();

    }

    @DisplayName("이미 방에 배치된 category가 입력되면 실패")
    @WithCustomUserDetails
    @Test
    public void user_stuff_이미_배치된_카테고리로_수정_실패() {
        // given
        Long[] userStuffId = new Long[2];
        for(int i = 0; i < 2; i++) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
            userStuffId[i] = userStuffService.saveUserStuff(requestDto);
        }
        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId[0]).category("IT").alias("Java 모음집1")
                .posX(1.1).posY(0.5).posZ(0.0).rotX(0.0).rotY(0.2).rotZ(2.2).build();
        userStuffId[0] = userStuffService.includeUserStuff(requestDto);

        // when
        UserStuffIncludeRequestDto requestDto2 = UserStuffIncludeRequestDto.builder()
                .id(userStuffId[1]).category("IT").alias("Java 모음집2")
                .posX(1.1).posY(0.5).posZ(0.0).rotX(0.0).rotY(0.2).rotZ(2.2).build();

        // then
        assertThatThrownBy(() -> { userStuffService.includeUserStuff(requestDto2);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.DUPLICATE_CATEGORY);
    }

    @DisplayName("stuff에 적용할 수 없는 category가 입력될 경우 실패")
    @WithCustomUserDetails
    @Test
    public void user_stuff_stuff에_적용할_수_없는_카테고리로_수정_실패() {
        // given
        UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDto);

        // when
        UserStuffIncludeRequestDto includeRequestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).category("BEAUTY").alias("Java 모음집2")
                .posX(1.1).posY(0.5).posZ(0.0).rotX(0.0).rotY(0.2).rotZ(2.2).build();

        // then
        assertThatThrownBy(() -> { userStuffService.includeUserStuff(includeRequestDto);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.UNACCEPTABLE_CATEGORY);
    }

    @DisplayName("UserStuff의 category 변경 시, 유효하지 않은 category를 입력할 경우 실패")
    @WithCustomUserDetails
    @Test
    public void user_stuff_category_변경_실패_1() {
        // given
        UserStuffSaveRequestDto requestDtoGiven = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(requestDtoGiven);

        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).category("IT").alias("Java 모음집")
                .posX(1.1).posY(0.5).posZ(0.0).rotX(0.0).rotY(0.2).rotZ(2.2).build();
        userStuffService.includeUserStuff(requestDto);

        // when
        UserStuffModifyRequestDto modifyRequestDto = UserStuffModifyRequestDto.builder()
                .id(userStuffId).category("JAVA").build();

        // then
        assertThatThrownBy(() -> {userStuffService.modifyAliasOrCategory(modifyRequestDto);})
                .isInstanceOf(CustomException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_VALID_CATEGORY);
    }

    @DisplayName("사용자가 보유하고 있는 UserStuff 중 배치된 UserStuff만 조회")
    @WithCustomUserDetails
    @Test
    public void 배치된_user_stuff_조회_성공() {
        // given
        List<String> categories = Arrays.asList("IT", "GAME", "SPORTS", "DAILY", "INTERIOR");
        for(int i = 0; i < stuffIds.size(); i++) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
            Long userStuffId = userStuffService.saveUserStuff(requestDto);
            if(i < stuffIds.size()/2) { // 5개만 배치
                UserStuffIncludeRequestDto arrangedDto = UserStuffIncludeRequestDto.builder()
                        .id(userStuffId).category(categories.get(i % categories.size())).alias("Java 모음집")
                        .posX(1.1).posY(0.5).posZ(0.0)
                        .rotX(0.0).rotY(0.2).rotZ(2.2)
                        .build();
                userStuffService.includeUserStuff(arrangedDto);
            }
        }

        // when,then
        List<UserStuffArrangedResponeDto> responseList = userStuffService.findArrangedUserStuffByUsername(username);
        assertThat(responseList.size()).isEqualTo(5);
    }

    @DisplayName("카테고리 타입이 NONE인 경우 여러개 배치 가능 성공")
    @WithCustomUserDetails
    @Test
    public void userstuff_category_none_배치_성공() {
        // given
        for(int i = 0; i < 3; i++) {
            String stuffName = "notebook" + (i + 1);
            String stuffNameKor = "노트북" + (i + 1);
            Long price = 30L;
            String sCategories = "NONE";
            String stuffType = "OTHER";

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .categories(sCategories)
                    .stuffType(stuffType)
                    .geometry("geometry")
                    .material("materials")
                    .build();
            Long stuffId = stuffRepository.save(requestDto.toEntity()).getId();
            Long userStuffId = userStuffService.saveUserStuff(UserStuffSaveRequestDto.builder()
                    .stuffId(stuffId).build());
            userStuffService.includeUserStuff(UserStuffIncludeRequestDto.builder()
                    .id(userStuffId).category("NONE").alias("NONE배치")
                    .posX(0.0).posY(0.0).posZ(0.0)
                    .rotX(0.0).rotY(0.0).rotZ(0.0).build());
        }

        // when, then
        List<UserStuffArrangedResponeDto> responseList = userStuffService.findArrangedUserStuffByUsername(username);
        assertThat(responseList.size()).isEqualTo(3);
    }

    @DisplayName("Stuff 구매 후 User Credit 차감 및 Payment, PaymentLine 기록")
    @WithCustomUserDetails
    @Test
    public void Stuff_구매_후_User_Credit_차감 () {
        //given
        UserStuffPurchaseRequestDto requestDto = new UserStuffPurchaseRequestDto(stuffIds);

        //when
        userStuffService.purchaseStuff(requestDto);

        //then
        User user = userRepository.findUserWithCredit(SecurityUtil.getCurrentUserId()).get();
        assertThat(credit).isGreaterThan(user.getCredit());
//        Payment payment = paymentRepository.findAll().get(0);
//        assertThat(payment.getUserId()).isEqualTo(SecurityUtil.getCurrentUserId());
//        assertThat(payment.getBeforeCredit()).isEqualTo(credit);
//        assertThat(payment.getAfterCredit()).isEqualTo(user.getCredit());
//        assertThat(payment.getTotalPrice()).isEqualTo(totalPrice);
    }

    @DisplayName("Stuff 구매 후 Payment 기록")
    @WithCustomUserDetails
    @Test
    public void Stuff_구매_후_Payment_기록 () {
        //given
        UserStuffPurchaseRequestDto requestDto = new UserStuffPurchaseRequestDto(stuffIds);

        //when
        userStuffService.purchaseStuff(requestDto);

        //then
        User user = userRepository.findUserWithCredit(SecurityUtil.getCurrentUserId()).get();
        Payment payment = paymentRepository.findAll().get(0);
        assertThat(payment.getUserId()).isEqualTo(SecurityUtil.getCurrentUserId());
        assertThat(payment.getBeforeCredit()).isEqualTo(credit);
        assertThat(payment.getAfterCredit()).isEqualTo(user.getCredit());
        assertThat(payment.getTotalPrice()).isEqualTo(totalPrice);
    }

    @DisplayName("Stuff 구매 후 PaymentLine 기록")
    @WithCustomUserDetails
    @Test
    public void Stuff_구매_후_PaymentLine_기록 () {
        //given
        UserStuffPurchaseRequestDto requestDto = new UserStuffPurchaseRequestDto(stuffIds);

        //when
        userStuffService.purchaseStuff(requestDto);

        //then
        Payment payment = paymentRepository.findAllPaymentByUser(SecurityUtil.getCurrentUserId()).get(0);
        List<PaymentLine> paymentLines = paymentLineRepository.findPaymentLinesByPaymentAndUser(payment);

        assertThat(
                paymentLines.stream().allMatch(paymentLine -> stuffIds.contains(paymentLine.getStuff().getId()))
        ).isTrue();

    }

    @DisplayName("로그인한 사용자의 배치된 UserStuff들의 카테고리 조회")
    @WithCustomUserDetails
    @Test
    public void 배치된_user_stuff의_카테고리_조회_성공() {
        // given
        List<String> categories = Arrays.asList("IT", "GAME", "NONE", "SPORTS", "DAILY");
        for(int i = 0; i < stuffIds.size(); i++) {
            UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffIds.get(i)).build();
            Long userStuffId = userStuffService.saveUserStuff(requestDto);
            if(i < stuffIds.size()/2) { // 5개만 배치
                UserStuffIncludeRequestDto arrangedDto = UserStuffIncludeRequestDto.builder()
                        .id(userStuffId).category(categories.get(i % categories.size())).alias("Java 모음집")
                        .posX(1.1).posY(0.5).posZ(0.0)
                        .rotX(0.0).rotY(0.2).rotZ(2.2)
                        .build();
                userStuffService.includeUserStuff(arrangedDto);
            }
        }

        // when,then
        List<UserStuffCategoryResponseDto> responseList = userStuffService.findCategoryPlacedInRoom();
        assertThat(responseList.size()).isEqualTo(4);
    }

}
