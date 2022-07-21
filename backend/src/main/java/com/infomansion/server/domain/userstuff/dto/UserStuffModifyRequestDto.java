package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.global.util.validation.ValidEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserStuffModifyRequestDto {

    @NotBlank
    private Long id;

    @ValidEnum(enumClass = Category.class, ignoreCase = true)
    private String category;
    private String alias;

    @Builder
    public UserStuffModifyRequestDto(Long id, String category, String alias) {
        this.id = id;
        this.category = category;
        this.alias = alias;
    }

    public UserStuff toEntity(UserStuff us) {
        return UserStuff.builder()
                .id(id)
                .user(us.getUser())
                .stuff(us.getStuff())
                .alias(alias == null ? us.getAlias() : alias)
                .category(category == null ? us.getCategory() : Category.valueOf(category))
                .selected(us.getSelected())
                .posX(us.getPosX()).posY(us.getPosY()).posZ(us.getPosZ())
                .rotX(us.getRotX()).rotY(us.getRotY()).rotZ(us.getRotZ()).build();
    }
}
