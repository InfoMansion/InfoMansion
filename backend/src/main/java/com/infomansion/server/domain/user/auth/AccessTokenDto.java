package com.infomansion.server.domain.user.auth;

import com.infomansion.server.global.util.jwt.TokenDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenDto {
    String accessToken;

    private AccessTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static AccessTokenDto of(TokenDto tokenDto) {
        return new AccessTokenDto(tokenDto.getAccessToken());
    }
}
