package com.infomansion.server.domain.user.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntityAtSoftDelete {

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

    private String profileImage;

    @Builder
    public User(String email, String password, String username, String tel, String categories) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
        this.authority = UserAuthority.ROLE_TEMP;
        this.categories = categories;
        this.profileImage = "/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png";
    }

    /**
     * 관심 카테고리 변경 메서드
     */
    public void changeCategories(String categories) {
        this.categories = categories;
    }

    /**
     * 임시 회원을 일반 회원으로 등급을 상승시키는 메서드
     */
    public void grantFromTempToUser() {
        this.authority = UserAuthority.ROLE_USER;
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
