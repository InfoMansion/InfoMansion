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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class UserStuffEditResponseDto {

    private Long userStuffId;
    private StuffType stuffType;

    private String alias;
    private Category selectedCategory;
    private List<CategoryMapperValue> categories;
    private Boolean selected;

    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal posZ;

    private BigDecimal rotX;
    private BigDecimal rotY;
    private BigDecimal rotZ;

    private String geometry;
    private String material;
    private String stuffGlbPath;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @Builder
    public UserStuffEditResponseDto(Long userStuffId, StuffType stuffType, String alias, Category selectedCategory, List<CategoryMapperValue> categories, Boolean selected, BigDecimal posX, BigDecimal posY, BigDecimal posZ, BigDecimal rotX, BigDecimal rotY, BigDecimal rotZ, String geometry, String material, String stuffGlbPath, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.userStuffId = userStuffId;
        this.stuffType = stuffType;
        this.alias = alias;
        this.selectedCategory = selectedCategory;
        this.categories = categories;
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

    public static UserStuffEditResponseDto toDto(UserStuff userStuff) {
        return UserStuffEditResponseDto.builder()
                .userStuffId(userStuff.getId())
                .stuffType(userStuff.getStuff().getStuffType())
                .alias(userStuff.getAlias())
                .selectedCategory(userStuff.getCategory())
                .categories(getCategories(userStuff.getStuff().getCategories()))
                .selected(userStuff.getSelected())
                .posX(userStuff.getPosX()).posY(userStuff.getPosY()).posZ(userStuff.getPosZ())
                .rotX(userStuff.getRotX()).rotY(userStuff.getRotY()).rotZ(userStuff.getRotZ())
                .geometry(userStuff.getStuff().getGeometry())
                .material(userStuff.getStuff().getMaterial())
                .modifiedTime(userStuff.getCreatedDate())
                .modifiedTime(userStuff.getModifiedDate())
                .build();
    }

    private static List<CategoryMapperValue> getCategories(String categories) {
        String[] splitCategories = categories.split(",");
        return Arrays.stream(splitCategories)
                .map(category -> new CategoryMapperValue(Category.valueOf(category)))
                .collect(Collectors.toList());
    }
}
