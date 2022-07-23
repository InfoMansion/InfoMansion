package com.infomansion.server.global.util.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReissueDto {
    @NotEmpty(message = "accessToken 을 입력해주세요.")
    private String accessToken;
    @NotEmpty(message = "refreshToken 을 입력해주세요.")
    private String refreshToken;
}
