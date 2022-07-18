package com.infomansion.server.domain.user.domain;

import com.infomansion.server.domain.category.Category;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

    @Column(length = 200)
    private String categories;

    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    @Builder
    public User(String email, String password, String username, String tel, String categories) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
        this.authority = UserAuthority.ROLE_USER;
        this.categories = categories;
    }


    /**
     * Admin으로 권한을 상승시키는 메서드
     */
    public void grantToAdmin() {
        this.authority = UserAuthority.ROLE_ADMIN;
    }

    /**
     * User의 password 재설정 메서드
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
