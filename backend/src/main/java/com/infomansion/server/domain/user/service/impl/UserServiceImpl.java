package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import com.infomansion.server.global.util.jwt.TokenProvider;
import com.infomansion.server.global.util.redis.RedisUtil;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final RedisUtil redisUtil;
    private final VerifyEmailService verifyEmailService;


    @Override
    @Transactional
    public Long join(UserSignUpRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        validateCategory(requestDto.getCategories());
        verifyEmailService.sendVerificationMail(requestDto.getEmail());
        return userRepository.save(requestDto.toEntityWithEncryptPassword(passwordEncoder)).getId();
    }

    @Override
    @Transactional
    public TokenDto login(UserLoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisUtil.setDataExpire("RT:"+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);
//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Override
    @Transactional
    public TokenDto reissue(ReissueDto reissueDto) {
        if(!tokenProvider.validateToken(reissueDto.getAccessToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(reissueDto.getAccessToken());

        String refreshToken = redisUtil.getData("RT:" + authentication.getName());

        if(!refreshToken.equals(reissueDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisUtil.setDataExpire("RT:"+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Override
    public boolean authBeforeChangePassword(UserAuthRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        return true;
    }

    @Override
    @Transactional
    public Long changePasswordAfterAuth(UserChangePasswordDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return user.getId();
    }

    @Transactional
    public Long changeCategories(UserChangeCategoriesDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validateCategory(requestDto.getCategories());
        user.changeCategories(requestDto.getCategories());

        return user.getId();
    }

    @Override
    @Transactional
    public boolean verifiedByEmail(String key) {
        String email = verifyEmailService.verifyEmail(key);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.grantFromTempToUser();
        return true;
    }

    private void validateCategory(String requestCategories) {
        List<String> categories = new ArrayList<>();
        for (Category value : Category.values()) {
            categories.add(value.name());
        }

        StringTokenizer st = new StringTokenizer(requestCategories,",");
        while(st.hasMoreTokens()) {
            if(!categories.contains(st.nextToken())) throw new CustomException(ErrorCode.NOT_VALID_CATEGORY);
        }
    }

    private void validateDuplicateUser(UserSignUpRequestDto requestDto) {
        // email 중복 검증
        if(userRepository.existsByEmail(requestDto.getEmail())) throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);

        // username 중복 검증
        if(userRepository.existsByUsername(requestDto.getUsername())) throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }
}
