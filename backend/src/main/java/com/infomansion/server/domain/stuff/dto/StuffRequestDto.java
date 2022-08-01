package com.infomansion.server.domain.stuff.dto;


import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.global.util.validation.ValidEnum;
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
public class StuffRequestDto {

    @NotBlank
    private String stuffName;
    @NotBlank
    private String stuffNameKor;
    @Min(0)
    @NotNull
    private Long price;
    @NotBlank
    private String categories;
    @ValidEnum(enumClass = StuffType.class, ignoreCase = true)
    @NotBlank
    private String stuffType;
    @NotBlank
    private String geometry;
    @NotBlank
    private String material;

    @Builder
    public StuffRequestDto(String stuffName, String stuffNameKor, Long price, String categories, String stuffType, String geometry, String material) {
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.categories = categories;
        this.stuffType = stuffType;
        this.geometry = geometry;
        this.material = material;
    }

    public Stuff toEntity() {
        return Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(StuffType.valueOf(stuffType))
                .geometry(geometry)
                .material(material)
                .build();
    }
}
