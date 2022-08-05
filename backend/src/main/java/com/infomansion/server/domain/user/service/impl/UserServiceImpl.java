package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.Room.repository.RoomRepository;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.domain.UserAuthority;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.infomansion.server.domain.category.util.CategoryUtil.validateCategories;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final VerifyEmailService verifyEmailService;
    private final S3Uploader s3Uploader;
    private final RoomRepository roomRepository;


    @Override
    @Transactional
    public Long join(UserSignUpRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        validateCategories(requestDto.getCategories());
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
    public UserModifyProfileResponseDto authBeforeChangePassword(UserAuthRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        return UserModifyProfileResponseDto.toDto(user);
    }

    @Override
    @Transactional
    public Long changePasswordAfterAuth(UserChangePasswordDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return user.getId();
    }

    @Override
    @Transactional
    public boolean verifiedByEmail(String key) {
        String email = verifyEmailService.verifyEmail(key);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.grantFromTempToUser();

        roomRepository.save(Room.createRoom(user));
        return true;
    }

    @Override
    @Transactional
    public boolean logout() {
        String data = redisUtil.getData("RT:" + SecurityUtil.getCurrentUserId());
        if(data == null) {
            // 유효하지 않은 accessToken을 통한 로그아웃 요청
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
        redisUtil.deleteData("RT:"+SecurityUtil.getCurrentUserId());
        return true;
    }

    @Override
    public UserInfoResponseDto findByUsername(String username) {
        return UserInfoResponseDto.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserSimpleProfileResponseDto findSimpleProfile() {
        return UserSimpleProfileResponseDto.toDto(userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public UserModifyProfileResponseDto modifyUserProfile(MultipartFile profileImage, UserModifyProfileRequestDto profileInfo) {
        if (userRepository.existsByUsername(profileInfo.getUsername()))
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);

        validateCategories(profileInfo.getCategories());

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        try {
            user.changeProfileImage(s3Uploader, profileImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.modifyProfile(profileInfo);
        return UserModifyProfileResponseDto.toDto(user);
    }

    private void validateDuplicateUser(UserSignUpRequestDto requestDto) {
        // email 중복 검증
        if(userRepository.existsByEmail(requestDto.getEmail())) throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);

        // username 중복 검증
        if(userRepository.existsByUsername(requestDto.getUsername())) throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }
}
