package com.infomansion.server.domain.userstuff.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class UserStuff extends BaseTimeEntityAtSoftDelete {

    @Id @GeneratedValue
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

    /**
     * 배치된 UserStuff의 Position과 Rotation 변경
     */
    public void changePosAndRot(BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void resetPosAndRot() {
        this.selected = false;
        this.posX = BigDecimal.ZERO;
        this.posY = BigDecimal.ZERO;
        this.posZ = BigDecimal.ZERO;
        this.rotX = BigDecimal.ZERO;
        this.rotY = BigDecimal.ZERO;
        this.rotZ = BigDecimal.ZERO;
    }

    /**
     * 배치된 UserStuff의 Alias 또는 Category 변경
     */
    public void changeAliasOrCategory(String alias, String category) {
        this.alias = alias == null ? this.alias : alias;
        this.category = category == null ? this.category : Category.valueOf(category);
    }

    /**
     * UserStuff를 배치된 상태로 변경
     */
    public void changeIncludedStatus(String alias, String category, BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ) {
        this.alias = alias;
        this.category = Category.valueOf(category);
        this.selected = true;
        changePosAndRot(posX, posY, posZ, rotX, rotY, rotZ);
    }

    /**
     * UserStuff 삭제 시 실제로 데이터를 삭제하지 않고 DeleteFlag를 통해 삭제되었다고 표시
     */
    public void deleteUserStuff() {
        this.deleteFlag = true;
        this.setDeletedDate();
        resetPosAndRot();
    }
}
