package com.infomansion.server.domain.user.service;

import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    Long join(UserSignUpRequestDto requestDto);
    TokenDto login(UserLoginRequestDto requestDto);
    TokenDto reissue(ReissueDto reissueDto);
    boolean authBeforeChangePassword(UserAuthRequestDto requestDto);
    Long changePasswordAfterAuth(UserChangePasswordDto requestDto);
    boolean verifiedByEmail(String key);
    boolean logout();
    UserInfoResponseDto findByUsername(String username);
    UserSimpleProfileResponseDto findSimpleProfile();
    Long modifyUserProfile(MultipartFile profileImage, UserModifyProfileDto profileInfo);
}
