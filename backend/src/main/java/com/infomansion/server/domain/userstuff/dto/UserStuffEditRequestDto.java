package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.domain.Category;
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
public class UserStuffEditRequestDto {

    @NotNull
    private Long userStuffId;

    @NotBlank
    private String alias;

    @ValidEnum(enumClass = Category.class, ignoreCase = true)
    @NotBlank
    private String selectedCategory;

    @Range(min = 0, max = 4)
    private BigDecimal posX;
    @Range(min = 0, max = 4)
    private BigDecimal posY;
    @Range(min = 0, max = 4)
    private BigDecimal posZ;

    @Range(min = 0, max = 7)
    private BigDecimal rotX;
    @Range(min = 0, max = 7)
    private BigDecimal rotY;
    @Range(min = 0, max = 7)
    private BigDecimal rotZ;

    @Builder
    public UserStuffEditRequestDto(Long userStuffId, String alias, String selectedCategory, Double posX, Double posY, Double posZ, Double rotX, Double rotY, Double rotZ) {
        this.userStuffId = userStuffId;
        this.alias = alias;
        this.selectedCategory = selectedCategory;
        this.posX = new BigDecimal(posX);
        this.posY = new BigDecimal(posY);
        this.posZ = new BigDecimal(posZ);
        this.rotX = new BigDecimal(rotX);
        this.rotY = new BigDecimal(rotY);
        this.rotZ = new BigDecimal(rotZ);
    }

}
