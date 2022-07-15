package com.infomansion.server.domain.user.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String username;
    private String tel;

    @Builder
    public User(String email, String password, String username, String tel) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
    }
}
