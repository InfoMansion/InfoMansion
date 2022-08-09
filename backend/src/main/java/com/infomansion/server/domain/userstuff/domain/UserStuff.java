package com.infomansion.server.domain.userstuff.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.dto.UserStuffEditRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class UserStuff extends BaseTimeEntityAtSoftDelete {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_stuff_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stuff_id")
    private Stuff stuff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String alias;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private Boolean selected;

    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal posZ;

    private BigDecimal rotX;
    private BigDecimal rotY;
    private BigDecimal rotZ;

    @OneToMany(mappedBy = "userStuff")
    private List<Post> postList = new ArrayList<>();

    private Boolean deleteFlag;

    @Builder
    public UserStuff(Long id, Stuff stuff, User user, String alias, Category category, Boolean selected, BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ) {
        this.id = id;
        this.stuff = stuff;
        this.user = user;
        this.alias = alias;
        this.category = category;
        this.selected = selected;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.deleteFlag = false;
    }

    private void changePosAndRot(BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    /**
     * UserStuff를 배치된 상태로 변경
     */
    public void changePlacedStatus(UserStuffEditRequestDto requestDto) {
        this.alias = requestDto.getAlias();
        if(this.category != Category.valueOf(requestDto.getCategory())) {
            changeCategoryOfPost(Category.valueOf(requestDto.getCategory()));
        }
        changeIsPublicOfPost(true);
        this.category = Category.valueOf(requestDto.getCategory());
        this.selected = true;
        changePosAndRot(requestDto.getPosX(), requestDto.getPosY(), requestDto.getPosZ(),
                requestDto.getRotX(), requestDto.getRotY(), requestDto.getRotZ());
    }

    public void changeExcludedState() {
        this.selected = false;
        changePosAndRot(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        changeIsPublicOfPost(false);
    }

    /**
     * 배치된 UserStuff의 Alias 또는 Category 변경
     */
    public void changeAliasOrCategory(String alias, String category) {
        this.alias = alias == null ? this.alias : alias;
        this.category = category == null ? this.category : Category.valueOf(category);
    }

    /**
     * UserStuff 삭제 시 실제로 데이터를 삭제하지 않고 DeleteFlag를 통해 삭제되었다고 표시
     */
    public void deleteUserStuff() {
        this.deleteFlag = true;
        this.setDeletedDate();
        this.getPostList().clear();
        changePosAndRot(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public void changeIsPublicOfPost(boolean isPublic) {
        postList.forEach(post -> post.updateIsPublic(isPublic));
    }

    public void changeCategoryOfPost(Category category) {
        postList.forEach(post -> post.updateCategory(category));
    }

    public static UserStuff havePossession(Stuff stuff, User user) {
        return UserStuff.builder()
                .stuff(stuff)
                .user(user)
                .alias(stuff.getStuffNameKor())
                .selected(false)
                .posX(BigDecimal.ZERO).posY(BigDecimal.ZERO).posZ(BigDecimal.ZERO)
                .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO)
                .build();
    }

    @Override
    public String toString() {
        return "UserStuff{" +
                "id=" + id +
                ", stuff=" + stuff.getStuffNameKor() +
                ", alias='" + alias + '\'' +
                ", category=" + category +
                '}';
    }

}
