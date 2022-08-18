package com.infomansion.server.domain.category.util;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryUtil {

    public static void validateCategories(String categories) {
        splitCategories(categories).forEach(CategoryUtil::validateOneCategory);
    }

    private static List<String> splitCategories(String categories) {
        List<String> splitCategories = Arrays.stream(categories.split(",")).collect(Collectors.toList());
        if(splitCategories.size() > 5) throw new CustomException(ErrorCode.EXCEEDED_THE_NUMBER_OF_CATEGORIES);

        return splitCategories;
    }

    private static void validateOneCategory(String category) {
        for (Category value : Category.values()) {
            if(category.equals(value.toString())) return;
        }
        throw new CustomException(ErrorCode.NOT_VALID_CATEGORY);
    }
}
