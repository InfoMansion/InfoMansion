package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.Stuff;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StoreResponseDto {

    private Long id;
    private String stuffNameKor;
    private Long price;
    private List<String> geometry;
    private List<String> material;
    private String stuffGlbPath;

    @Builder
    public StoreResponseDto(Long id, String stuffNameKor, Long price, String geometry, String material, String stuffGlbPath) {
        this.id = id;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.geometry = Arrays.stream(geometry.split(",")).collect(Collectors.toList());
        this.material = Arrays.stream(material.split(",")).collect(Collectors.toList());
        this.stuffGlbPath = stuffGlbPath;
    }

    public static StoreResponseDto toResponseDto(Stuff stuff) {
        return StoreResponseDto.builder()
                .id(stuff.getId())
                .stuffNameKor(stuff.getStuffNameKor())
                .price(stuff.getPrice())
                .geometry(stuff.getGeometry())
                .material(stuff.getMaterial())
                .stuffGlbPath(stuff.getStuffGlbPath())
                .build();
    }
}
