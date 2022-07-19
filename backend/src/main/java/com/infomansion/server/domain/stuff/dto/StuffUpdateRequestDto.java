package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
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
    private String category;
    @NotBlank
    private String stuffType;

    @Builder
    public StuffUpdateRequestDto(Long id, String stuffName, String stuffNameKor, Long price, String category, String stuffType) {
        this.id = id;
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.category = category;
        this.stuffType = stuffType;
    }

    public Stuff toEntity() {
        return Stuff.builder()
                .id(id)
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .category(Category.valueOf(category))
                .stuffType(StuffType.valueOf(stuffType))
                .build();
    }
}
