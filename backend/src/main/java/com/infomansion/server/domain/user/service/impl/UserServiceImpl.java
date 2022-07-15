package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserLoginRequestDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import com.infomansion.server.global.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    @Override
    @Transactional
    public Long join(UserSignUpRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        return userRepository.save(requestDto.toEntityWithEncryptPassword(passwordEncoder)).getId();
    }

    @Override
    @Transactional
    public TokenDto login(UserLoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Override
    @Transactional
    public TokenDto reissue(ReissueDto reissueDto) {
        if(!tokenProvider.validateToken(reissueDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(reissueDto.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if(!refreshToken.equals(reissueDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }


    private void validateDuplicateUser(UserSignUpRequestDto requestDto) {
        // email 중복 검증
        if(userRepository.existsByEmail(requestDto.getEmail())) throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);

        // username 중복 검증
        if(userRepository.existsByUsername(requestDto.getUsername())) throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }
}
