package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
public class UserStuffResponseDto {

    private Long userStuffId;

    private String stuffName;
    private String stuffNameKor;
    private StuffType stuffType;

    private String alias;
    private Category category;
    private Boolean selected;

    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal posZ;

    private BigDecimal rotX;
    private BigDecimal rotY;
    private BigDecimal rotZ;

    public UserStuffResponseDto(UserStuff userStuff) {
        this.userStuffId = userStuff.getId();
        this.stuffName = userStuff.getStuff().getStuffName();
        this.stuffNameKor = userStuff.getStuff().getStuffNameKor();
        this.stuffType = userStuff.getStuff().getStuffType();
        this.alias = userStuff.getAlias();
        this.category = userStuff.getCategory();
        this.selected = userStuff.getSelected();
        this.posX = userStuff.getPosX();
        this.posY = userStuff.getPosY();
        this.posZ = userStuff.getPosZ();
        this.rotX = userStuff.getRotX();
        this.rotY = userStuff.getRotY();
        this.rotZ = userStuff.getRotZ();
    }
}
