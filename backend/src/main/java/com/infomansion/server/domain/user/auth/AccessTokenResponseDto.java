package com.infomansion.server.domain.user.auth;

import com.infomansion.server.global.util.jwt.TokenDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenResponseDto {
    private String accessToken;
    private Date expiresAt;

    public AccessTokenResponseDto(TokenDto tokenDto) {
        this.accessToken = tokenDto.getAccessToken();
        this.expiresAt = tokenDto.getAccessTokenExpiresTime();
    }
}
