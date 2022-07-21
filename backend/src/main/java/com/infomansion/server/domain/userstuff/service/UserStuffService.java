package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffModifyRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;

import java.util.List;

public interface UserStuffService {

    Long saveUserStuff(UserStuffRequestDto requestDto);

    List<UserStuffResponseDto> findAllUserStuff(Long userId);

    UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId);

    Long excludeUserStuff(Long userStuffId);

    Long includeUserStuff(UserStuffIncludeRequestDto requestDto);

    Long modifyAliasAndCategory(UserStuffModifyRequestDto requestDto);

}
