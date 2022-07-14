package com.infomansion.server.global.apispec;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CommonResponse<T> extends BasicResponse {

    private boolean success;
    private String message;
    private T data;

    public CommonResponse(T data) {
        this.success = true;
        this.data = data;
        this.message = "ok";
    }

    public CommonResponse<T> ok(T data) {
        return new CommonResponse<>(data);
    }

}
