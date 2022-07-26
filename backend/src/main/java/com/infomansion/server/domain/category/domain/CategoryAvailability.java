package com.infomansion.server.domain.category.domain;

public class CategoryAvailability {

    private String category;
    private String categoryName;
    private Boolean isAvailable;

    public CategoryAvailability(CategoryMapperType enumMapperType, Boolean isAvailable) {
        category = enumMapperType.getCategory();
        categoryName = enumMapperType.getCategoryName();
        this.isAvailable = isAvailable;
    }
}
