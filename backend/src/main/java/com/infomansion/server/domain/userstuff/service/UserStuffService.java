package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.category.domain.CategoryAvailability;
import com.infomansion.server.domain.userstuff.dto.*;

import java.util.List;

public interface UserStuffService {

    Long saveUserStuff(UserStuffRequestDto requestDto);

    List<UserStuffResponseDto> findAllUserStuff(Long userId);

    UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId);

    Long excludeUserStuff(Long userStuffId);

    Long includeUserStuff(UserStuffIncludeRequestDto requestDto);

    Long modifyAliasAndCategory(UserStuffModifyRequestDto requestDto);

    Long modifyPosAndRot(UserStuffPositionRequestDto requestDto);

    Long removeUserStuff(Long userStuffId);

    List<CategoryAvailability> getCategoriesAvailableInUserStuff(Long userStuffId);

}
