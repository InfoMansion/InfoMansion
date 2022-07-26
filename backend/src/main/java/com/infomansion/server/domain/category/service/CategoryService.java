package com.infomansion.server.domain.category.service;

import com.infomansion.server.domain.category.domain.CategoryMapperValue;

import java.util.List;

public interface CategoryService {

    List<CategoryMapperValue> findAllCategory();
}
