package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private String categories;
    private String profileImage;
    private String introduce;

    @Builder
    protected UserInfoResponseDto(Long userId, String username, String categories, String profileImage, String introduce) {
        this.userId = userId;
        this.username = username;
        this.categories = categories;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    public static UserInfoResponseDto toDto(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .categories(user.getCategories())
                .profileImage(user.getProfileImage())
                .introduce(user.getIntroduce())
                .build();
    }
}
