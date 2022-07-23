package com.infomansion.server.domain.user.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenRequestDto {
    @NotBlank
    private String accessToken;

    public AccessTokenRequestDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
