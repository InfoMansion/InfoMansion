package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private List<String> categories = new ArrayList<>();
    private String profileImage;
    private String introduce;
    private boolean isLoginUser;

    @Builder
    protected UserInfoResponseDto(Long userId, String username, String categories, String profileImage, String introduce, boolean isLoginUser) {
        this.userId = userId;
        this.username = username;
        this.categories = Arrays.stream(categories.split(",")).collect(Collectors.toList());
        this.profileImage = profileImage;
        this.introduce = introduce;
        this.isLoginUser = isLoginUser;
    }

    public static UserInfoResponseDto toDto(User user, Long loginUserId) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .categories(user.getCategories())
                .profileImage(user.getProfileImage())
                .introduce(user.getIntroduce())
                .isLoginUser(user.getId() == loginUserId ? true : false)
                .build();
    }
}
