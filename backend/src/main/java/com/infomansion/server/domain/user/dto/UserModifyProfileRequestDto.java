package com.infomansion.server.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserModifyProfileRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String categories;

    private String introduce;
}
