package com.infomansion.server.domain.userstuff.service;

import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.dto.*;

import java.util.List;

public interface UserStuffService {

    Long saveUserStuff(UserStuffSaveRequestDto requestDto);

    List<UserStuffEditResponseDto> findAllUserStuff();

    UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId);

    Long modifyAliasOrCategory(UserStuffModifyRequestDto requestDto);

    Long removeUserStuff(Long userStuffId);

    List<UserStuffArrangedResponeDto> findArrangedUserStuffByUsername(String username);

    void saveDefaultUserStuff(User user);
    List<StuffResponseDto> purchaseStuff(UserStuffPurchaseRequestDto requestDto);

    List<UserStuffCategoryResponseDto> findCategoryPlacedInRoom();

    boolean editUserStuff(List<UserStuffEditRequestDto> requestDtos);

}
