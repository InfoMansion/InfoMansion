package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.dto.*;

import java.util.List;

public interface UserStuffService {

    Long saveUserStuff(UserStuffSaveRequestDto requestDto);

    List<UserStuffResponseDto> findAllUserStuff();

    UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId);

    Long excludeUserStuff(Long userStuffId);

    Long includeUserStuff(UserStuffIncludeRequestDto requestDto);

    Long modifyAliasOrCategory(UserStuffModifyRequestDto requestDto);

    Long modifyPosAndRot(UserStuffPositionRequestDto requestDto);

    Long removeUserStuff(Long userStuffId);

    List<UserStuffArrangedResponeDto> findArrangedUserStuffByUsername(String username);

    void saveDefaultUserStuff(User user);
    List<StuffResponseDto> purchaseStuff(UserStuffPurchaseRequestDto requestDto);

    List<UserStuffCategoryResponseDto> findCategoryPlacedInRoom();

}
