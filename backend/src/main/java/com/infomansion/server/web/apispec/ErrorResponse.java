package com.infomansion.server.web.apispec;

import com.infomansion.server.util.exception.CustomException;
import com.infomansion.server.util.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private boolean success;
    private final HttpStatus status;
    private final String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.success = false;
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

    protected void setSuccess() {
        this.success = true;
    }

    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }
}
