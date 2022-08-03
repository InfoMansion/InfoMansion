package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserModifyProfileResponseDto {

    private String email;
    private String username;
    private List<String> categories = new ArrayList<>();
    private String introduce;
    private String profileImageUrl;

    @Builder
    public UserModifyProfileResponseDto(String email, String username, List<String> categories, String introduce, String profileImageUrl) {
        this.email = email;
        this.username = username;
        this.categories = categories;
        this.introduce = introduce;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserModifyProfileResponseDto toDto (User user) {
        return UserModifyProfileResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .introduce(user.getIntroduce())
                .profileImageUrl(
                        "https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com" +
                        user.getProfileImage())
                .categories(Arrays.stream(user.getCategories().split(",")).collect(Collectors.toList()))
                .build();
    }

}
