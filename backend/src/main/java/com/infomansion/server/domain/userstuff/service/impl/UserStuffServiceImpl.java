package com.infomansion.server.domain.userstuff.service.impl;

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

        checkDuplicatePlacedCategory(findUserStuff.getUser().getId(), requestDto.getCategory());
        checkAcceptableCategory(findUserStuff.getStuff(), requestDto.getCategory());

        findUserStuff.changeIncludedStatus(requestDto.getAlias(), requestDto.getCategory(),
                requestDto.getPosX(), requestDto.getPosY(), requestDto.getPosZ(),
                requestDto.getRotX(), requestDto.getRotY(), requestDto.getRotZ());
        return findUserStuff.getId();
    }

    @Transactional
    @Override
    public Long modifyAliasOrCategory(UserStuffModifyRequestDto requestDto) {
         // Alias와 Category 둘 다 입력하지 않았다면 throw
        if(requestDto.getCategory() == null && requestDto.getAlias() == null)
            throw new CustomException(ErrorCode.NULL_VALUE_OF_ALIAS_AND_CATEGORY);

        // category가 null이 아닐 경우 Category Enum에 존재하는 값인지 체크
        // Category가 null일 경우 Alias만 변경하는 경우이기 때문에 아무런 오류처리를 하지 않는다.
        requestDto.isValidEnum();

        UserStuff us = userStuffRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        // 배치되지 않은 UserStuff의 Alias나 Category를 변경할 경우 throw
        if(!us.getSelected()) throw new CustomException(ErrorCode.EXCLUDED_USER_STUFF);

        // category가 null이 아닐 경우 새로운 category로 변경할 수 있는지 검증
        if(requestDto.getCategory() != null) {
            checkDuplicatePlacedCategory(us.getUser().getId(), requestDto.getCategory());
            checkAcceptableCategory(us.getStuff(), requestDto.getCategory());
        }

        // category가 null일 경우 changeAliasOrCategory에서 변경이 일어나지 않고 기존 category를 사용한다.
        // alias가 null일 경우 changeAliasOrCategory에서 변경이 일어나지 않고 기존 alias를 사용한다.
        us.changeAliasOrCategory(requestDto.getAlias(), requestDto.getCategory());
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

    private void checkDuplicatePlacedCategory(Long userId, String category) {
        if(userStuffRepository.findAllCategoryByUserId(userId).contains(category))
            throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
    }

    private void checkAcceptableCategory(Stuff stuff, String category) {
        if(!stuff.getCategoryList().contains(category))
            throw new CustomException(ErrorCode.UNACCEPTABLE_CATEGORY);
    }


}
