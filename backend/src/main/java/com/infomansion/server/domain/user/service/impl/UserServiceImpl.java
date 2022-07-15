package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Long join(UserSignUpRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        return userRepository.save(requestDto.toEntityWithEncryptPassword(passwordEncoder)).getId();
    }

    private void validateDuplicateUser(UserSignUpRequestDto requestDto) {
        // email 중복 검증
        List<User> usersByEmail = userRepository.findUsersByEmail(requestDto.getEmail());
        if(!usersByEmail.isEmpty()) throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);

        // username 중복 검증
        List<User> usersByUsername = userRepository.findUsersByUsername(requestDto.getUsername());
        if(!usersByUsername.isEmpty()) throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }
}
