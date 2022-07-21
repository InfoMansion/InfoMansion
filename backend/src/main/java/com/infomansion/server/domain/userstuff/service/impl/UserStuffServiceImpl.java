package com.infomansion.server.domain.userstuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserStuffServiceImpl implements UserStuffService {

    private final UserRepository userRepository;
    private final StuffRepository stuffRepository;
    private final UserStuffRepository userStuffRepository;

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


}
