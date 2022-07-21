package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.global.util.validation.ValidEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffIncludeRequestDto {

    @NotBlank
    private Long id;

    private String alias;

    @ValidEnum(enumClass = Category.class, ignoreCase = true)
    @NotBlank
    private String category;

    @Range(min = 0L, max = 4L)
    private Float posX;
    @Range(min = 0L, max = 4L)
    private Float posY;
    @Range(min = 0L, max = 4L)
    private Float posZ;

    @Range(min = 0L, max = 2L)
    private Float rotX;
    @Range(min = 0L, max = 2L)
    private Float rotY;
    @Range(min = 0L, max = 2L)
    private Float rotZ;

    @Builder
    public UserStuffIncludeRequestDto(Long id, String alias, String category, Float posX, Float posY, Float posZ, Float rotX, Float rotY, Float rotZ) {
        this.id = id;
        this.alias = alias;
        this.category = category;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public UserStuff toEntity(UserStuff us) {
        return UserStuff.builder()
                .id(id)
                .stuff(us.getStuff())
                .user(us.getUser())
                .alias(alias)
                .category(Category.valueOf(category))
                .selected(true)
                .posX(posX).posY(posY).posZ(posZ)
                .rotX(rotX).rotY(rotY).rotZ(rotZ).build();
    }
}
