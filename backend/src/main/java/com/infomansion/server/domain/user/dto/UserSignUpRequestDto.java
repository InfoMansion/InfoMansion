package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    @Email @NotBlank
    private String email;
    @NotBlank @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 8~20자로 영문, 숫자, 특수문자를 적어도 하나 사용해야 됩니다")
    private String password;
    @NotBlank @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9_]{3,15}$", message = "닉네임은 3~15자로 영문, 숫자, 특수문자('_')를 사용할 수 있습니다.")
    private String username;
    @NotBlank
    private String tel;

    @NotBlank
    private String categories;

    @Builder
    public UserSignUpRequestDto(String email, String password, String username, String tel, String categories) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
        this.categories = categories;
    }

    public User toEntityWithEncryptPassword(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .tel(tel)
                .username(username)
                .categories(categories)
                .build();
    }
}
