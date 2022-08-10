package com.infomansion.server.global.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //== 400 ==//
    // USER
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST,40001, "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST,40002, "중복된 사용자 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,40003, "중복된 사용자 닉네임입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 40004, "비밀번호가 올바르지 않습니다."),
    NOT_VALID_USER(HttpStatus.BAD_REQUEST, 40005, "Room을 생성할 수 없는 권한을 가진 User입니다."),
    NOT_ENOUGH_CREDIT(HttpStatus.BAD_REQUEST, 40006, "크레딧이 부족합니다."),
    NEGATIVE_AMOUNT_OF_CREDIT(HttpStatus.BAD_REQUEST, 40007, "음수인 크레딧입니다"),
    NOT_PUBLIC_USER(HttpStatus.BAD_REQUEST, 40008, "비공개 사용자의 계정입니다."),
    USER_NO_PERMISSION(HttpStatus.BAD_REQUEST, 40009, "권한이 없는 사용자입니다."),

    // JWT
    NOT_VALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,40020, "유효하지 않은 Refresh Token입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, 40021, "만료된 JWT 토큰입니다."),

    // VALIDATION
    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST, 40030,"지원하지 않는 Http Method 방식입니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST,40031,"유효하지 않은 Request Body 혹은 Argument입니다."),

    // CATEGORY
    NOT_VALID_CATEGORY(HttpStatus.BAD_REQUEST, 40040, "유효하지 않은 카테고리입니다."),
    EXCEEDED_THE_NUMBER_OF_CATEGORIES(HttpStatus.BAD_REQUEST, 40041, "설정 가능한 카테고리 수를 초과하였습니다."),
    UNACCEPTABLE_CATEGORY(HttpStatus.BAD_REQUEST, 40042, "해당 Stuff에 적용할 수 없는 카테고리입니다."),
    DUPLICATE_CATEGORY(HttpStatus.BAD_REQUEST, 40043, "이미 방에 존재하는 카테고리입니다."),

    // STUFF
    STUFF_NOT_FOUND(HttpStatus.BAD_REQUEST, 40050, "해당 Stuff id를 찾을 수 없습니다."),

    // USERSTUFF
    USER_STUFF_NOT_FOUND(HttpStatus.BAD_REQUEST, 40060, "유효하지 않은 Stuff입니다."),
    EXCLUDED_USER_STUFF(HttpStatus.BAD_REQUEST, 40061, "제외된 Stuff입니다."),
    INCLUDED_USER_STUFF(HttpStatus.BAD_REQUEST, 40062, "배치된 Stuff입니다."),
    NULL_VALUE_OF_ALIAS_AND_CATEGORY(HttpStatus.BAD_REQUEST, 40063, "별칭 또는 카테고리 값이 필요합니다."),

    //ROOM
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, 40070, "해당 RoomId를 찾을 수 없습니다."),

    //POST
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, 40080, "해당 Post가 존재하지 않습니다."),

    // FOLLOW
    ALREADY_FOLLOW_USER(HttpStatus.BAD_REQUEST, 40090, "이미 팔로우한 유저입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.BAD_REQUEST, 40091, "팔로우 관계를 찾을 수 없습니다."),


    //== 401 ==//
    TOKEN_WITHOUT_AUTHORITY(HttpStatus.UNAUTHORIZED,40101, "권한 정보가 없는 토큰입니다."),
    EMPTY_CREDENTIALS(HttpStatus.UNAUTHORIZED,40102, "인증 정보가 없습니다."),

    //== 404 ==//
    VERIFICATION_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "인증 키를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final int code;
    private final String message;

}
