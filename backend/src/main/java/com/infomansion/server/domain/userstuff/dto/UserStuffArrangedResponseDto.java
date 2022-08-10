package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class UserStuffArrangedResponseDto {

    private Long id;
    private String stuffNameKor;
    private String alias;
    private CategoryMapperValue category;
    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal posZ;
    private BigDecimal rotX;
    private BigDecimal rotY;
    private BigDecimal rotZ;
    private List<String> geometry;
    private List<String> material;
    private String stuffGlbPath;
    private String backgroundByWall;

    @Builder
    public UserStuffArrangedResponseDto(Long id, String stuffNameKor, String alias, Category category, BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ, List<String> geometry, List<String> material, String stuffGlbPath, String backgroundByWall) {
        this.id = id;
        this.stuffNameKor = stuffNameKor;
        this.alias = alias;
        this.category = new CategoryMapperValue(category);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.geometry = geometry;
        this.material = material;
        this.stuffGlbPath = stuffGlbPath;
        this.backgroundByWall = backgroundByWall;
    }

    public static UserStuffArrangedResponseDto toDto(UserStuff userStuff) {
        return UserStuffArrangedResponseDto.builder()
                .id(userStuff.getId())
                .stuffNameKor(userStuff.getStuff().getStuffNameKor())
                .alias(userStuff.getAlias())
                .category(userStuff.getCategory())
                .posX(userStuff.getPosX())
                .posY(userStuff.getPosY())
                .posZ(userStuff.getPosZ())
                .rotX(userStuff.getRotX())
                .rotY(userStuff.getRotY())
                .rotZ(userStuff.getRotZ())
                .geometry(userStuff.getStuff().getGeometryList())
                .material(userStuff.getStuff().getMaterialList())
                .stuffGlbPath(userStuff.getStuff().getStuffGlbPath())
                .backgroundByWall(userStuff.getStuff().getBackgroundByWall())
                .build();
    }
}
