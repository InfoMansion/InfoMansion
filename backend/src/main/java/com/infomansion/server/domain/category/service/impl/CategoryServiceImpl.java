package com.infomansion.server.domain.category.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.category.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<CategoryMapperValue> findAllCategory() {
        return Arrays.stream(Category.values())
                .map(category -> new CategoryMapperValue(category))
                .collect(Collectors.toList());
    }
}
