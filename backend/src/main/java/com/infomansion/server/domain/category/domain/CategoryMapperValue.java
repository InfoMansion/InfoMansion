package com.infomansion.server.domain.category.domain;

import com.infomansion.server.global.util.common.EnumMapperType;
import lombok.Getter;

@Getter
public class CategoryMapperValue {

    private String category;
    private String categoryName;

    public CategoryMapperValue(EnumMapperType enumMapperType) {
        category = enumMapperType.getEnum();
        categoryName = enumMapperType.getEnumName();
    }
}
