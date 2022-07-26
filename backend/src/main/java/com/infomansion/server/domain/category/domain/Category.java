package com.infomansion.server.domain.category.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category implements CategoryMapperType {
    IT("IT"),
    COOK("요리"),
    FASHIONANDBEAUTY("패션/뷰티"),
    GAME("게임"),
    SPORTS("스포츠"),
    DAILY("데일리"),
    TRAVEL("여행"),
    ART("미술"),
    MUSIC("음악"),
    INTERIOR("인테리어"),
    NATURE("자연"),
    CLEANING("청소"),
    CULTURE("문화/시사"),
    STUDY("공부"),
    HOMEAPPLIANCES("가전제품"),
    NONE("NONE");

    private final String categoryName;

    @Override
    public String getCategory() {
        return this.name();
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }
}