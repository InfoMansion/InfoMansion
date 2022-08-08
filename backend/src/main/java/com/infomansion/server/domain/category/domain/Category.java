package com.infomansion.server.domain.category.domain;

import com.infomansion.server.global.util.common.EnumMapperType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category implements EnumMapperType {
    IT("IT"),
    COOK("요리"),
    FASHION("패션"),
    BEAUTY("뷰티"),
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
    GUESTBOOK("방명록"),
    NONE("NONE");

    private final String categoryName;
    private final String categoryDefaultThumbnail=
            "https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/thumbnail/"
                    +getEnum()+"_defaultThumbnail.jpeg";

    @Override
    public String getEnum() {
        return this.name();
    }

    @Override
    public String getEnumName() {
        return this.categoryName;
    }

    public String getCategoryDefaultThumbnail(){
        return this.categoryDefaultThumbnail;
    }
}