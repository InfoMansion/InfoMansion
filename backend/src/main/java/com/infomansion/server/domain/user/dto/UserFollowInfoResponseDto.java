package com.infomansion.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserFollowInfoResponseDto {

    private String username;
    private String profileImage;
    private boolean followFlag;

    @Builder
    public UserFollowInfoResponseDto(String username, String profileImage, boolean followFlag) {
        this.username = username;
        this.profileImage = profileImage;
        this.followFlag = followFlag;
    }

    public static UserFollowInfoResponseDto toResponseDto(Object[] result) {
        return UserFollowInfoResponseDto.builder()
                .username(result[0].toString())
                .profileImage(result[1].toString())
                .followFlag(((Integer) result[2]) == 1 ? true : false)
                .build();
    }
}
