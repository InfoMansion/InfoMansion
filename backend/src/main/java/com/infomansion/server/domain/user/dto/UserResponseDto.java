package com.infomansion.server.domain.user.dto;

import com.infomansion.server.domain.user.domain.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;
    private String tel;
    private String categories;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.tel = user.getTel();
        this.categories = user.getCategories();
    }
}
