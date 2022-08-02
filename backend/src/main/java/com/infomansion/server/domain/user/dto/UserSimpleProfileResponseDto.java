package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleProfileResponseDto {

    private String username;
    private String profileImage;

    @Builder
    protected UserSimpleProfileResponseDto(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }

    public static UserSimpleProfileResponseDto toDto(User user) {
        return UserSimpleProfileResponseDto.builder()
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .build();
    }

}
