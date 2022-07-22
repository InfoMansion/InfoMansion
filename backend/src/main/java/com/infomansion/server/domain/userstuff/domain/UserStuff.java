package com.infomansion.server.domain.userstuff.domain;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Float posX;
    private Float posY;
    private Float posZ;

    private Float rotX;
    private Float rotY;
    private Float rotZ;

    @Builder
    public UserStuff(Long id, Stuff stuff, User user, String alias, Category category, Boolean selected, Float posX, Float posY, Float posZ, Float rotX, Float rotY, Float rotZ) {
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

}
