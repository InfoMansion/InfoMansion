package com.infomansion.server.global.apispec;

import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@RequiredArgsConstructor
public class ErrorResponse extends BasicResponse {

    private boolean success;
    private int code;
    private String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.success = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    protected ErrorResponse(ErrorCode errorCode, String message) {
        this.success = false;
        this.code = errorCode.getCode();
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }

    public static ResponseEntity<ErrorResponse> error(CustomException e, String message) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode(), message));
    }
}
