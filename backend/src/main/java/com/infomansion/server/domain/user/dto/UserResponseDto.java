package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.domain.UserAuthority;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
public class UserResponseDto {

    private Long id;
    private String email;
    private String password;
    private String username;
    private String tel;
    private String categories;
    private UserAuthority authority;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.tel = user.getTel();
        this.categories = user.getCategories();
        this.authority = user.getAuthority();
    }
}
