package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class StuffResponseDto {

    private Long id;
    private String stuffName;
    private String stuffNameKor;
    private Long price;
    private List<CategoryMapperValue> categories;
    private StuffType stuffType;
    private List<String> geometry;
    private List<String> material;
    private String stuffGlbPath;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public StuffResponseDto(Stuff stuff) {
        this.id = stuff.getId();
        this.stuffName = stuff.getStuffName();
        this.stuffNameKor = stuff.getStuffNameKor();
        this.price = stuff.getPrice();
        this.categories = getCategories(stuff.getCategories());
        this.stuffType = stuff.getStuffType();
        this.geometry = stuff.getGeometryList();
        this.material = stuff.getMaterialList();
        this.stuffGlbPath = stuff.getStuffGlbPath();
        this.createdTime = stuff.getCreatedDate();
        this.modifiedTime = stuff.getModifiedDate();
    }

    public List<CategoryMapperValue> getCategories(String categories) {
        String[] splitCategories = categories.split(",");
        return Arrays.stream(splitCategories)
                .map(category -> new CategoryMapperValue(Category.valueOf(category)))
                .collect(Collectors.toList());
    }
}
