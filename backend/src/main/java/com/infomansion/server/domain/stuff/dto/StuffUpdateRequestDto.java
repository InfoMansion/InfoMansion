package com.infomansion.server.domain.stuff.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class StuffUpdateRequestDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String stuffName;
    @NotBlank
    private String stuffNameKor;
    @Min(0)
    @NotNull
    private Long price;
    @NotBlank
    private String categories;
    @NotBlank
    private String stuffType;
    @NotBlank
    private String geometry;
    @NotBlank
    private String material;

    @Builder
    public StuffUpdateRequestDto(Long id, String stuffName, String stuffNameKor, Long price, String categories, String stuffType, String geometry, String material) {
        this.id = id;
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.categories = categories;
        this.stuffType = stuffType;
        this.geometry = geometry;
        this.material = material;
    }

}
