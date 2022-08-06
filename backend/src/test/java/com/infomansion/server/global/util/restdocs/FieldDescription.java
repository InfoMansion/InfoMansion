package com.infomansion.server.global.util.restdocs;

import org.springframework.restdocs.payload.JsonFieldType;

public enum FieldDescription {

    // Stuff

    STUFF_ID(JsonFieldType.NUMBER,"Stuff Id"),
    STUFF_NAME(JsonFieldType.STRING, "Stuff 영문명"),
    STUFF_NAME_KOR(JsonFieldType.STRING, "Stuff 한글명"),
    STUFF_PRICE(JsonFieldType.NUMBER, "Stuff 가격"),
    STUFF_CATEGORIES(JsonFieldType.ARRAY, "Stuff에 적용할 수 있는 카테고리"),
    STUFF_TYPE(JsonFieldType.STRING, "Stuff의 가구 타입"),
    STUFF_TYPE_NAME(JsonFieldType.STRING, "Stuff 가구 타입 한글명"),
    GEOMETRY(JsonFieldType.STRING, "3D 모델 모양"),
    MATERIAL(JsonFieldType.STRING, "3D 모델 색상"),
    STUFF_GLB_PATH(JsonFieldType.STRING, "Stuff glb파일 주소"),
    STUFF_CREATED(JsonFieldType.STRING, "Stuff 생성날짜"),
    STUFF_MODIFIED(JsonFieldType.STRING, "Stuff 마지막 수정날짜"),

    // category
    CATEGORY(JsonFieldType.STRING, "카테고리 영문명"),
    CATEGORY_NAME(JsonFieldType.STRING, "카테고리 한글명"),

    // slice
    SLICE(JsonFieldType.OBJECT, "페이징된 목록"),
    SLICE_CONTENT(JsonFieldType.ARRAY, "페이징된 content"),
    SLICE_NUMBER_OF_ELEMENTS(JsonFieldType.NUMBER, "content의 size"),
    SLICE_FIRST(JsonFieldType.BOOLEAN, "첫 페이지 여부"),
    SLICE_LAST(JsonFieldType.BOOLEAN, "마지막 페이지 여부"),
    SLICE_NUMBER(JsonFieldType.NUMBER, "현재 페이지"),
    SLICE_SIZE(JsonFieldType.NUMBER, "페이지 당 개수"),

    // user
    USER_ID(JsonFieldType.NUMBER, "사용자 id"),
    USERNAME(JsonFieldType.STRING, "사용자 닉네임"),
    USER_EMAIL(JsonFieldType.STRING, "사용자 이메일"),
    USER_CATEGORIES(JsonFieldType.ARRAY, "사용자가 설정한 관심 카테고리"),
    PROFILE_IMAGE(JsonFieldType.STRING, "사용자 프로필 이미지 url"),
    INTRODUCE(JsonFieldType.STRING, "사용자 자기소개"),

    // userStuff
    USERSTUFF_ID(JsonFieldType.NUMBER, "userStuff Id"),
    ALIAS(JsonFieldType.STRING, "userStuff 별칭"),
    USERSTUFF_SELECTED(JsonFieldType.BOOLEAN, "방에 배치가 되었는 지"),
    POS_X(JsonFieldType.NUMBER,"위치 좌표 X값"),
    POS_Y(JsonFieldType.NUMBER,"위치 좌표 Y값"),
    POS_Z(JsonFieldType.NUMBER,"위치 좌표 Z값"),
    ROT_X(JsonFieldType.NUMBER,"회전 X값"),
    ROT_Y(JsonFieldType.NUMBER,"회전 Y값"),
    ROT_Z(JsonFieldType.NUMBER,"회전 Z값"),
    USERSTUFF_CREATED(JsonFieldType.STRING, "UserStuff 생성날짜"),
    USERSTUFF_MODIFIED(JsonFieldType.STRING, "UserStuff 마지막 수정날짜"),

    // Token
    ACCESSTOKEN(JsonFieldType.STRING, "accessToken 정보"),
    REFRESHTOKEN(JsonFieldType.STRING, "refreshToken 정보"),
    ACCESSTOKEN_EXPIRE(JsonFieldType.STRING, "accessToken 만료기간"),

    // Post
    POST_ID(JsonFieldType.NUMBER, "post Id"),
    POST_TITLE(JsonFieldType.STRING, "title"),
    POST_CONTENT(JsonFieldType.STRING, "content"),
    LIKES_POST(JsonFieldType.NUMBER, "좋아요 수"),

    // Base
    MODIFIED_DATE(JsonFieldType.STRING, "마지막 수정날짜"),

    ;

    private final JsonFieldType jsonFieldType;
    private final String description;

    FieldDescription(JsonFieldType jsonFieldType, String description) {
        this.jsonFieldType = jsonFieldType;
        this.description = description;
    }

    public JsonFieldType getJsonFieldType() {
        return this.jsonFieldType;
    }

    public String getDescription() {
        return this.description;
    }
}
