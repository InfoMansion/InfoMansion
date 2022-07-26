package com.infomansion.server.domain.userstuff.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryAvailability;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserStuffServiceImpl implements UserStuffService {

    private final UserRepository userRepository;
    private final StuffRepository stuffRepository;
    private final UserStuffRepository userStuffRepository;

    @Transactional
    @Override
    public Long saveUserStuff(UserStuffRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Stuff stuff = stuffRepository.findById(requestDto.getStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));

        return userStuffRepository.save(requestDto.toEntity(user, stuff)).getId();
    }

    @Override
    public List<UserStuffResponseDto> findAllUserStuff(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return userStuffRepository.findByUser(user).stream()
                .map(userStuff -> new UserStuffResponseDto(userStuff))
                .collect(Collectors.toList());
    }

    @Override
    public UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId) {
        UserStuff findUserStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        return new UserStuffResponseDto(findUserStuff);
    }

    @Transactional
    @Override
    public Long excludeUserStuff(Long userStuffId) {
        UserStuff findUserStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        /**
         * 이미 제외된 Stuff를 요청하는 경우 throw
         */
        if(!findUserStuff.getSelected()) throw new CustomException(ErrorCode.EXCLUDED_USER_STUFF);

        findUserStuff.resetPosAndRot();
        return userStuffId;
    }

    @Transactional
    @Override
    public Long includeUserStuff(UserStuffIncludeRequestDto requestDto) {
        UserStuff findUserStuff = userStuffRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        /**
         * 이미 배치된 Stuff를 요청하는 경우 throw
         */
        if(findUserStuff.getSelected()) throw new CustomException(ErrorCode.INCLUDED_USER_STUFF);

        validateCategory(findUserStuff.getStuff(), requestDto.getCategory());

        findUserStuff.changeIncludedStatus(requestDto.getAlias(), requestDto.getCategory(),
                requestDto.getPosX(), requestDto.getPosY(), requestDto.getPosZ(),
                requestDto.getRotX(), requestDto.getRotY(), requestDto.getRotZ());
        return findUserStuff.getId();
    }

    @Transactional
    @Override
    public Long modifyAliasAndCategory(UserStuffModifyRequestDto requestDto) {
        /**
         * Alias와 Category 둘 다 입력하지 않았다면 throw
         */
        if(requestDto.getCategory() == null && requestDto.getAlias() == null)
            throw new CustomException(ErrorCode.NULL_VALUE_OF_ALIAS_AND_CATEGORY);

        UserStuff us = userStuffRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        requestDto.isValidEnum();
        if(requestDto.getCategory() != null) validateCategory(us.getStuff(), requestDto.getCategory());

        /**
         * 배치되지 않은 Stuff의 Alias나 Category를 변경할 경우 throw
         */
        if(!us.getSelected()) throw new CustomException(ErrorCode.EXCLUDED_USER_STUFF);

        us.changeAliasAndCategory(requestDto.getAlias(), requestDto.getCategory());
        return us.getId();
    }

    @Transactional
    @Override
    public Long modifyPosAndRot(UserStuffPositionRequestDto requestDto) {
        UserStuff userStuff = userStuffRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        /**
         * 배치되지 않은 Stuff의 Position과 Rotation을 변경할 경우 throw
         */
        if(!userStuff.getSelected()) throw new CustomException(ErrorCode.EXCLUDED_USER_STUFF);

        userStuff.changePosAndRot(requestDto.getPosX(), requestDto.getPosY(), requestDto.getPosZ(),
                requestDto.getRotX(), requestDto.getRotY(), requestDto.getRotZ());

        return userStuff.getId();
    }

    @Transactional
    @Override
    public Long removeUserStuff(Long userStuffId) {
        UserStuff userStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        userStuff.deleteUserStuff();
        return userStuffId;
    }

    @Override
    public List<CategoryAvailability> getCategoriesAvailableInUserStuff(Long userStuffId) {
        UserStuff userStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        List<String> categoriesInUse = userStuffRepository.findAllCategory();
        return userStuff.getStuff().getCategoryList()
                .stream().map(category -> {
                    if(categoriesInUse.contains(category))
                        return new CategoryAvailability(Category.valueOf(category), false);
                    else
                        return new CategoryAvailability(Category.valueOf(category), true);
                }).collect(Collectors.toList());
    }

    /**
     * 1. 배치된 UserStuff의 카테고리들과 중복된 게 있을 경우 DUPLICATE_CATEGORY error
     * 2. UserStuff의 Stuff가 가질 수 없는 카테고리일 경우 UNACCEPTABLE_CATEGORY error
     */
    private void validateCategory(Stuff stuff, String category) {
        if(userStuffRepository.findAllCategory().contains(category))
            throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
        if(stuff.getCategoryList().contains(category))
            throw new CustomException(ErrorCode.UNACCEPTABLE_CATEGORY);
    }


}
