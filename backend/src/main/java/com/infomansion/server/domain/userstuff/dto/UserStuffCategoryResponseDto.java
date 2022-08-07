package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserStuffCategoryResponseDto {

    private String stuffNameKor;
    private String alias;
    private CategoryMapperValue category;

    @Builder
    public UserStuffCategoryResponseDto(String stuffNameKor, String alias, CategoryMapperValue category) {
        this.stuffNameKor = stuffNameKor;
        this.alias = alias;
        this.category = category;
    }

    public static UserStuffCategoryResponseDto toResponseDto(UserStuff userStuff) {
        return UserStuffCategoryResponseDto.builder()
                .stuffNameKor(userStuff.getStuff().getStuffNameKor())
                .alias(userStuff.getAlias())
                .category(new CategoryMapperValue(userStuff.getCategory()))
                .build();
    }
}
