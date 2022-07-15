package com.infomansion.server.global.util.jwt;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReissueDto {
    @NotEmpty(message = "accessToken 을 입력해주세요.")
    private String accessToken;
    @NotEmpty(message = "refreshToken 을 입력해주세요.")
    private String refreshToken;
}
