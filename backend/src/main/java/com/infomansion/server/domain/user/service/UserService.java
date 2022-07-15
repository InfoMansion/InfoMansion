package com.infomansion.server.domain.user.service;

import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;

public interface UserService {

    Long join(UserSignUpRequestDto requestDto);
}
