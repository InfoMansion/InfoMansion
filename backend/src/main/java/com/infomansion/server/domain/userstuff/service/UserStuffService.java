package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;

import java.util.List;

public interface UserStuffService {

    Long saveUserStuff(UserStuffRequestDto requestDto);

    List<UserStuffResponseDto> findAllUserStuff(Long userId);

    UserStuffResponseDto findUserStuffById(UserStuffRequestDto requestDto);


}
