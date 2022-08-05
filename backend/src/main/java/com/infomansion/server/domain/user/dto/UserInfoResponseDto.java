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
    private String userEmail;
    private String username;
    private List<String> categories = new ArrayList<>();
    private String profileImage;
    private String introduce;

    @Builder
    protected UserInfoResponseDto(Long userId, String userEmail, String username, String categories, String profileImage, String introduce) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.username = username;
        this.categories = Arrays.stream(categories.split(",")).collect(Collectors.toList());
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    public static UserInfoResponseDto toDto(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .userEmail(user.getEmail())
                .username(user.getUsername())
                .categories(user.getCategories())
                .profileImage(user.getProfileImage())
                .introduce(user.getIntroduce())
                .build();
    }
}
