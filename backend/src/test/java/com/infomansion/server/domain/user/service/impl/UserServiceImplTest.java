package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.notification.domain.Notification;
import com.infomansion.server.domain.notification.repository.NotificationRepository;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.domain.user.repository.FollowRepository;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private VerifyEmailService verifyEmailService;

    @MockBean
    private S3Uploader s3Uploader;

    @BeforeEach
    public void setUp() {
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        userService.join(requestDto);

//        userRepository.save(User.builder()
//                .email(email)
//                .password(password)
//                .tel(tel)
//                .username(username)
//                .categories(categories)
//                .build());
    }

    @AfterEach
    public void cleanUp() {
        followRepository.deleteAll();
        userRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    public void 비밀번호_암호화() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when
        Long userId = userService.join(requestDto);

        //then
        Optional<User> findUser = userRepository.findById(userId);
        assertThat(findUser.get().getPassword()).isNotEqualTo(password);
    }

    @DisplayName("닉네임이 같은 사용자는 회원가입이 불가능합니다.")
    @Test
    public void 중복_회원_검증_닉네임() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when & then
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(CustomException.class);

    }

    @DisplayName("이메일이 같은 사용자는 회원가입이 불가능합니다.")
    @Test
    public void 중복_회원_검증_이메일() {
        //given
        String email = "infomansion@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        String categories = "IT,COOK";
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when & then
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(CustomException.class);

    }

    @Test
    public void 회원가입시_기본프로필이미지제공() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String tel = "01012345678";
        String username = "testUsername1";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();

        //when
        Long userId = userService.join(requestDto);

        //then
        assertThat(userRepository.findById(userId).get().getProfileImage()).isEqualTo("https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png");
    }

    @WithCustomUserDetails
    @Test
    public void 사용자이름으로_회원조회시_UserInfoResponseDto_반환() {
        //given&when
        UserInfoResponseDto responseDto = userService.findByUsername("infomansion");

        //then
        assertThat(responseDto.getUsername()).isEqualTo("infomansion");
        assertThat(responseDto.getCategories().size()).isEqualTo(2);
        assertThat(responseDto.getCategories().get(0)).isEqualTo("IT");
        assertThat(responseDto.getCategories().get(1)).isEqualTo("COOK");
        assertThat(responseDto.getProfileImage()).isNotNull();
        assertThat(responseDto.getUserId()).isNotNull();
        assertThat(responseDto.getIntroduce()).isNotNull();

    }

    @WithCustomUserDetails
    @Test
    public void 로그인된_회원의_프로필이미지_조회() {
        //given&when
        UserSimpleProfileResponseDto responseDto = userService.findSimpleProfile();

        //then
        assertThat(responseDto.getUsername()).isEqualTo("infomansion");
        assertThat(responseDto.getProfileImage()).isNotNull();
        assertThat(responseDto.getProfileImage()).isEqualTo("https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png");
    }

    @WithCustomUserDetails
    @Test
    public void 사용자_프로필_수정() {
        //given
        UserModifyProfileRequestDto requestDto = new UserModifyProfileRequestDto("testUsername", "GAME", "simple introduce");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("hi", new byte[]{});

        //when
        UserModifyProfileResponseDto modifiedUser = userService.modifyUserProfile(mockMultipartFile, requestDto);

        //then
        User findUser = userRepository.findByUsername(modifiedUser.getUsername()).get();

        assertThat(findUser.getCategories()).isEqualTo("GAME");
        assertThat(findUser.getUsername()).isEqualTo("testUsername");
        assertThat(findUser.getIntroduce()).isEqualTo("simple introduce");
    }

    @Test
    public void 사용자_프로필_수정_실패_존재하지않는카테고리() {
        //given
        UserModifyProfileRequestDto requestDto = new UserModifyProfileRequestDto("testUsername", "NOCATEGORY", "simple introduce");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("hi", new byte[]{});

        //when&then
        assertThatThrownBy(() -> userService.modifyUserProfile(mockMultipartFile, requestDto))
                .isInstanceOf(CustomException.class);

    }

    @Test
    public void 사용자_프로필_수정_실패_중복된유저네임() {
        //given
        UserModifyProfileRequestDto requestDto = new UserModifyProfileRequestDto("infomansion", "NOCATEGORY", "simple introduce");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("hi", new byte[]{});

        //when&then
        assertThatThrownBy(() -> userService.modifyUserProfile(mockMultipartFile, requestDto))
                .isInstanceOf(CustomException.class);

    }

    @WithCustomUserDetails
    @Test
    public void 사용자정보변경_전_비밀번호인증_성공() {
        //given
        UserAuthRequestDto requestDto = new UserAuthRequestDto("testPassword1$");

        //when
        UserModifyProfileResponseDto responseDto = userService.authBeforeChangePassword(requestDto);

        //then
        assertThat(responseDto.getEmail()).isEqualTo("infomansion@test.com");
        assertThat(responseDto.getUsername()).isEqualTo("infomansion");
        assertThat(responseDto.getCategories().size()).isEqualTo(2);
        assertThat(responseDto.getCategories().get(0)).isEqualTo("IT");
        assertThat(responseDto.getCategories().get(1)).isEqualTo("COOK");
    }

    @WithCustomUserDetails
    @Test
    public void 사용자정보변경_전_비밀번호인증_실패_올바르지않은_비밀번호() {
        //given
        UserAuthRequestDto requestDto = new UserAuthRequestDto("testPassword1@");

        //when&then
        assertThatThrownBy(() -> userService.authBeforeChangePassword(requestDto))
                .isInstanceOf(CustomException.class);
    }

    @WithCustomUserDetails
    @Test
    public void 다른_사용자_팔로우_성공() {
        // given
        for (int i = 1; i <= 5; i++) {
            String email = "infomansion" + i + "@test.com";
            String password = "testPassword1$";
            String tel = "01012345678";
            String username = "infomansion" + i;
            String categories = "IT,COOK";

            UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                    .email(email)
                    .password(password)
                    .tel(tel)
                    .username(username)
                    .categories(categories)
                    .build();
            userService.join(requestDto);

            // when
            if (i % 2 == 0) userService.followUser(username);
        }

        // then
        List<UserFollowInfoResponseDto> followingUserList = userService.findFollowingUserList("infomansion");
        assertThat(followingUserList.size()).isEqualTo(2);
    }

    @WithCustomUserDetails
    @Test
    public void 다른_사용자_언팔로우_성공() {
        // given
        for (int i = 1; i <= 5; i++) {
            String email = "infomansion" + i + "@test.com";
            String password = "testPassword1$";
            String tel = "01012345678";
            String username = "infomansion" + i+"";
            String categories = "IT,COOK";

            UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                    .email(email)
                    .password(password)
                    .tel(tel)
                    .username(username)
                    .categories(categories)
                    .build();
            userService.join(requestDto);
            if (i % 2 == 0) userService.followUser(username);
        }

        // when
        boolean response = userService.unFollowUser("infomansion2");

        // then
        assertThat(response).isTrue();
        assertThat(userService.findFollowingUserList("infomansion").size()).isEqualTo(1);
    }

    @WithCustomUserDetails
    @Test
    public void 사용자_팔로우_리스트_조회_성공() {
        // given
        for (int i = 1; i <= 5; i++) {
            String email = "infomansion" + i + "@test.com";
            String password = "testPassword1$";
            String tel = "01012345678";
            String username = "infomansion" + i;
            String categories = "IT,COOK";

            UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                    .email(email)
                    .password(password)
                    .tel(tel)
                    .username(username)
                    .categories(categories)
                    .build();
            userService.join(requestDto);
            userService.followUser(username);
        }

        // when
        List<UserFollowInfoResponseDto> response = userService.findFollowingUserList("infomansion");
        // then
        assertThat(response.size()).isEqualTo(5);
        for(int i = 0; i < 5; i++)
            assertThat(response.get(i).getUsername()).isEqualTo("infomansion"+(i+1));
    }

    @WithCustomUserDetails
    @Test
    public void 사용자_팔로워_리스트_조회_성공() {
        // given
        String email = "infomansion1@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion1";
        String categories = "IT,COOK";

        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();
        userService.join(requestDto);
        userService.followUser(username);

        // when
        List<UserFollowInfoResponseDto> response = userService.findFollowerUserList(username);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getUsername()).isEqualTo("infomansion");
    }

    @WithCustomUserDetails
    @Test
    public void 사용자_팔로우시_Noti_생성() {
        // given
        for (int i = 1; i <= 5; i++) {
            String email = "infomansion" + i + "@test.com";
            String password = "testPassword1$";
            String tel = "01012345678";
            String username = "infomansion" + i;
            String categories = "IT,COOK";

            UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                    .email(email)
                    .password(password)
                    .tel(tel)
                    .username(username)
                    .categories(categories)
                    .build();
            userService.join(requestDto);

            // when
            if (i % 2 == 0) userService.followUser(username);
        }

        // then
        List<Notification> notificationsToInfomansion1 = notificationRepository.findSimpleUnReadNotificationsByUsername("infomansion1");
        List<Notification> notificationsToInfomansion2 = notificationRepository.findSimpleUnReadNotificationsByUsername("infomansion2");
        assertThat(notificationsToInfomansion1.size()).isEqualTo(0);
        assertThat(notificationsToInfomansion2.size()).isEqualTo(1);
    }

    @WithCustomUserDetails
    @Transactional
    @Test
    public void 사용자_회원탈퇴_성공() {

        // when
        Optional<User> beforeDeleteUser = userRepository.findByUsername("infomansion");
        assertThat(beforeDeleteUser.get().getUsername()).isEqualTo("infomansion");

        userService.deleteUser();
        Optional<User> afterDeleteUser = userRepository.findByUsername("infomansion");

        // then
        assertThat(afterDeleteUser).isEmpty();
    }
}