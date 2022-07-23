package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.global.util.validation.ValidEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffIncludeRequestDto {

    @NotNull
    private Long id;

    private String alias;

    @ValidEnum(enumClass = Category.class, ignoreCase = true)
    @NotBlank
    private String category;

    @Range(min = 0, max = 4)
    private BigDecimal posX;
    @Range(min = 0, max = 4)
    private BigDecimal posY;
    @Range(min = 0, max = 4)
    private BigDecimal posZ;

    @Range(min = 0, max = 2)
    private BigDecimal rotX;
    @Range(min = 0, max = 2)
    private BigDecimal rotY;
    @Range(min = 0, max = 2)
    private BigDecimal rotZ;

    @Builder
    public UserStuffIncludeRequestDto(Long id, String alias, String category, Double posX, Double posY, Double posZ, Double rotX, Double rotY, Double rotZ) {
        this.id = id;
        this.alias = alias;
        this.category = category;
        this.posX = new BigDecimal(posX);
        this.posY = new BigDecimal(posY);
        this.posZ = new BigDecimal(posZ);
        this.rotX = new BigDecimal(rotX);
        this.rotY = new BigDecimal(rotY);
        this.rotZ = new BigDecimal(rotZ);
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
