package com.infomansion.server.domain.stuff.dto;


import com.infomansion.server.domain.stuff.domain.Category;
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
    @ValidEnum(enumClass = Category.class, ignoreCase = true, message = "유효하지 않은 카테고리입니다.")
    @NotBlank
    private String category;
    @ValidEnum(enumClass = StuffType.class, ignoreCase = true, message = "유효하지 않은 타입입니다.")
    @NotBlank
    private String stuffType;

    @Builder
    public StuffRequestDto(String stuffName, String stuffNameKor, Long price, String category, String stuffType) {
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.category = category;
        this.stuffType = stuffType;
    }

    public Stuff toEntity() {
        return Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .category(Category.valueOf(category))
                .stuffType(StuffType.valueOf(stuffType))
                .build();
    }
}
