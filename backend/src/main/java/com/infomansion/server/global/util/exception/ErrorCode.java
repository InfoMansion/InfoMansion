package com.infomansion.server.global.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //== 400 ==//
    // USER
    DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST, "중복된 사용자 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 사용자 닉네임입니다."),

    // JWT
    TOKEN_WITHOUT_AUTHORITY(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    EMPTY_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 정보가 없습니다."),

    // VALIDATION
    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST,"지원하지 않는 Http Method 방식입니다."),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST,"유효하지 않은 Request Body 혹은 Argument입니다.");

    private final HttpStatus status;
    private final String message;

}
