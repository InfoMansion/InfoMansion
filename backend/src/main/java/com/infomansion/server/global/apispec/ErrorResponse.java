package com.infomansion.server.global.apispec;

import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ErrorResponse extends BasicResponse {

    private boolean success;
    private String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.success = false;
        this.message = errorCode.getMessage();
    }

    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }
}
