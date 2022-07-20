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

    // JWT
    NOT_VALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,40020, "유효하지 않은 Refresh Token입니다."),
    TOKEN_WITHOUT_AUTHORITY(HttpStatus.UNAUTHORIZED,40021, "권한 정보가 없는 토큰입니다."),
    EMPTY_CREDENTIALS(HttpStatus.UNAUTHORIZED,40022, "인증 정보가 없습니다."),

    // VALIDATION
    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST, 40030,"지원하지 않는 Http Method 방식입니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST,40031,"유효하지 않은 Request Body 혹은 Argument입니다."),

    // CATEGORY
    NOT_VALID_CATEGORY(HttpStatus.BAD_REQUEST, 40040, "유효하지 않은 카테고리입니다."),

    // STUFF
    STUFF_NOT_FOUND(HttpStatus.BAD_REQUEST, 40050, "해당 Stuff id를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;

}
