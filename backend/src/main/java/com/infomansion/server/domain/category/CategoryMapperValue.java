package com.infomansion.server.domain.category;

import lombok.Getter;

@Getter
public class CategoryMapperValue {

    private String category;
    private String categoryName;

    public CategoryMapperValue(CategoryMapperType enumMapperType) {
        category = enumMapperType.getCategory();
        categoryName = enumMapperType.getCategoryName();
    }
}
