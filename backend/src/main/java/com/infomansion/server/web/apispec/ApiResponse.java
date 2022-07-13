package com.infomansion.server.web.apispec;

import com.infomansion.server.util.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> extends ErrorResponse {

    private final T data;

    private ApiResponse(T data) {
        super(ErrorCode.SUCCESS);
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(response);
    }
}
