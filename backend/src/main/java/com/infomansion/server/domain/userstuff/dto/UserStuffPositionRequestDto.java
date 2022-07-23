package com.infomansion.server.domain.userstuff.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffPositionRequestDto {

    @NotNull
    private Long id;

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
    public UserStuffPositionRequestDto(Long id, Double posX, Double posY, Double posZ, Double rotX, Double rotY, Double rotZ) {
        this.id = id;
        this.posX = new BigDecimal(posX);
        this.posY = new BigDecimal(posY);
        this.posZ = new BigDecimal(posZ);
        this.rotX = new BigDecimal(rotX);
        this.rotY = new BigDecimal(rotY);
        this.rotZ = new BigDecimal(rotZ);
    }
}
