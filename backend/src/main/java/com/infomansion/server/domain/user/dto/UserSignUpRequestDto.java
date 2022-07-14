package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    @NotBlank
    private String tel;

    @Builder
    public UserSignUpRequestDto(String email, String password, String username, String tel) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .build();
    }
}
