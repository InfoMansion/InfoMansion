package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffModifyRequestDto {

    @NotNull
    private Long id;

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

    public void isValidCategory() {
        if(category == null) return;
        String upperCategory = category.toUpperCase();
        for (Category c : Category.values()) {
            if(upperCategory.equals(c.toString())) {
                category = c.toString();
                return;
            }
        }
        throw new CustomException(ErrorCode.NOT_VALID_CATEGORY);
    }
}
