package com.infomansion.server.domain.user.service;

import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;

public interface UserService {

    Long join(UserSignUpRequestDto requestDto);
    TokenDto login(UserLoginRequestDto requestDto);
    TokenDto reissue(ReissueDto reissueDto);
    boolean authBeforeChangePassword(UserAuthRequestDto requestDto);
    Long changePasswordAfterAuth(UserChangePasswordDto requestDto);
    Long changeCategories(UserChangeCategoriesDto requestDto);
}
