package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.Stuff;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponseDto {

    private Long id;
    private String stuffNameKor;
    private Long price;
    private String geometry;
    private String material;
    private String stuffGlbPath;

    @Builder
    public StoreResponseDto(Long id, String stuffNameKor, Long price, String geometry, String material, String stuffGlbPath) {
        this.id = id;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.geometry = geometry;
        this.material = material;
        this.stuffGlbPath = stuffGlbPath;
    }

    public StoreResponseDto(Stuff stuff) {
        this.id = stuff.getId();
        this.stuffNameKor = stuff.getStuffNameKor();
        this.price = stuff.getPrice();
        this.geometry = stuff.getGeometry();
        this.material = stuff.getMaterial();
        this.stuffGlbPath = stuff.getStuffGlbPath();
    }
}
