package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import lombok.Getter;

@Getter
public class StoreResponseDto {

    private Long id;
    private String stuffNameKor;
    private Long price;
    private String geometry;
    private String materials;
    private String stuffGlbPath;

    public StoreResponseDto(Stuff stuff) {
        this.id = stuff.getId();
        this.stuffNameKor = stuff.getStuffNameKor();
        this.price = stuff.getPrice();
        this.geometry = stuff.getGeometry();
        this.materials = stuff.getMaterials();
        this.stuffGlbPath = stuff.getStuffGlbPath();
    }
}
