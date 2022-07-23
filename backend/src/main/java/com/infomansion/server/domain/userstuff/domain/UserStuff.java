package com.infomansion.server.domain.userstuff.domain;

import com.infomansion.server.domain.category.Category;
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
public class UserStuff {

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
}
