package com.infomansion.server.web.controller;

import com.infomansion.server.util.exception.CustomException;
import com.infomansion.server.util.exception.ErrorCode;
import com.infomansion.server.web.apispec.ApiResponse;
import com.infomansion.server.web.apispec.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiResponseTestController {

    @GetMapping("/test/success")
    public ApiResponse<?> success() {
        return ApiResponse.ok("test");
    }

    @GetMapping("/test/error")
    public ResponseEntity<ErrorResponse> error() {
        return ErrorResponse.error(new CustomException(ErrorCode.NOT_SUPPORTED_HTTP_METHOD));
    }
}
