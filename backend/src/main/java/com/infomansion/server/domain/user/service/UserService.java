package com.infomansion.server.domain.user.service;

import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    Long join(UserSignUpRequestDto requestDto);
    TokenDto login(UserLoginRequestDto requestDto);
    TokenDto reissue(ReissueDto reissueDto);
    UserModifyProfileResponseDto authBeforeChangePassword(UserAuthRequestDto requestDto);
    Long changePasswordAfterAuth(UserChangePasswordDto requestDto);
    boolean verifiedByEmail(String key);
    boolean logout();
    UserInfoResponseDto findByUsername(String username);
    UserSimpleProfileResponseDto findSimpleProfile();
    UserModifyProfileResponseDto modifyUserProfile(MultipartFile profileImage, UserModifyProfileRequestDto profileInfo);
    boolean resetPassword(UserResetPasswordRequestDto requestDto, String redirectURL);
    UserSearchResponseDto findUserBySearchWordForUserName(String searchWord, Pageable pageable);
    boolean followUser(String username);
    boolean unFollowUser(String username);
    List<UserFollowInfoResponseDto> findFollowerUserList(String username);
    List<UserFollowInfoResponseDto> findFollowingUserList(String username);
    Boolean changeUserState(String username);
    Long deleteUser();
    UserCreditInfoResponseDto findUserCredit();
}
