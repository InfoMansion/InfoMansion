package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class UserStuffResponseDto {

    private Long userStuffId;
    private StuffType stuffType;

    private String alias;
    private CategoryMapperValue category;
    private Boolean selected;

    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal posZ;

    private BigDecimal rotX;
    private BigDecimal rotY;
    private BigDecimal rotZ;

    private List<String> geometry;
    private List<String> material;
    private String stuffGlbPath;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @Builder
    public UserStuffResponseDto(Long userStuffId, StuffType stuffType, String alias, Category category, Boolean selected, BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ, List<String> geometry, List<String> material, String stuffGlbPath, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.userStuffId = userStuffId;
        this.stuffType = stuffType;
        this.alias = alias;
        if(category != null)
            this.category = new CategoryMapperValue(category);
        this.selected = selected;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.geometry = geometry;
        this.material = material;
        this.stuffGlbPath = stuffGlbPath;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public static UserStuffResponseDto toDto(UserStuff userStuff) {
        return UserStuffResponseDto.builder()
                .userStuffId(userStuff.getId())
                .stuffType(userStuff.getStuff().getStuffType())
                .alias(userStuff.getAlias())
                .category(userStuff.getCategory())
                .selected(userStuff.getSelected())
                .posX(userStuff.getPosX()).posY(userStuff.getPosY()).posZ(userStuff.getPosZ())
                .rotX(userStuff.getRotX()).rotY(userStuff.getRotY()).rotZ(userStuff.getRotZ())
                .geometry(userStuff.getStuff().getGeometryList())
                .material(userStuff.getStuff().getMaterialList())
                .modifiedTime(userStuff.getCreatedDate())
                .modifiedTime(userStuff.getModifiedDate())
                .build();
    }
}
