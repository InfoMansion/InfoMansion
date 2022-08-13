package com.infomansion.server.domain.user.dto;

import lombok.Getter;

@Getter
public class UserCreditInfoResponseDto {

    private Long credit;

    public UserCreditInfoResponseDto(Long credit) {
        this.credit = credit;
    }
}
