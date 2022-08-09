package com.infomansion.server.domain.user.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.dto.UserModifyProfileRequestDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntityAtSoftDelete {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(length = 200)
    private String introduce;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<Follow> follower = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserCredit userCredit;

    private boolean privateFlag;

    @Builder
    public User(String email, String password, String username, String tel, String categories) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
        this.authority = UserAuthority.ROLE_TEMP;
        this.categories = categories;
        this.profileImage = "https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png";
        this.introduce = "";
        this.userCredit = new UserCredit(this);
        this.privateFlag = false;
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

    /**
     * User 프로필 정보 변경 [username, categories, introduce]
     */
    public void modifyProfile(UserModifyProfileRequestDto profileDto) {
        this.username = profileDto.getUsername();
        this.categories = profileDto.getCategories();
        this.introduce = profileDto.getIntroduce();
    }

    public void changeProfileImage(S3Uploader s3Uploader, MultipartFile multipartFile) throws IOException {
        if(multipartFile != null) {
            this.profileImage = s3Uploader.uploadFiles(multipartFile, "profile");
        }
    }

    /**
     * User의 크레딧을 조회합니다.
     * @return userCredit
     */
    public Long getCredit() {
        return this.userCredit.getCredit();
    }

    /**
     * Stuff 구매하여 Credit이 감소합니다.
     */
    public void purchaseStuff(Long stuffPrice) {
        this.userCredit.spend(stuffPrice);
    }

    /**
     * User의 공개 권한을 비공개로 바꾼다.
     */
    public void changePrivate(){
        this.privateFlag = !this.privateFlag;
    }
}
