package com.infomansion.server.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    IT("IT"), COOK("요리"), BEAUTY("뷰티"), GAME("게임");

    private final String categoryName;
}
