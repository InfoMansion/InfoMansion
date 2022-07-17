package com.infomansion.server.domain.user.domain;

import lombok.*;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    @Builder
    public User(String email, String password, String username, String tel) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
        this.authority = UserAuthority.ROLE_USER;
    }

    /**
     * Admin으로 권한을 상승시키는 메서드
     */
    public void grantToAdmin() {
        this.authority = UserAuthority.ROLE_ADMIN;
    }
}
