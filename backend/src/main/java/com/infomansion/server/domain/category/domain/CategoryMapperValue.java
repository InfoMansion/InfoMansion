package com.infomansion.server.domain.category.domain;

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
