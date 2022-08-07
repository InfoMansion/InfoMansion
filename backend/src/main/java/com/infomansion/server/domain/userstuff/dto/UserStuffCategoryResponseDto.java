package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserStuffCategoryResponseDto {

    private Long userStuffId;
    private String stuffNameKor;
    private String alias;
    private CategoryMapperValue category;

    @Builder
    public UserStuffCategoryResponseDto(Long userStuffId, String stuffNameKor, String alias, CategoryMapperValue category) {
        this.userStuffId = userStuffId;
        this.stuffNameKor = stuffNameKor;
        this.alias = alias;
        this.category = category;
    }

    public static UserStuffCategoryResponseDto toResponseDto(UserStuff userStuff) {
        return UserStuffCategoryResponseDto.builder()
                .userStuffId(userStuff.getId())
                .stuffNameKor(userStuff.getStuff().getStuffNameKor())
                .alias(userStuff.getAlias())
                .category(new CategoryMapperValue(userStuff.getCategory()))
                .build();
    }
}
