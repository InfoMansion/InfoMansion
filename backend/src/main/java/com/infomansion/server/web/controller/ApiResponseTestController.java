package com.infomansion.server.web.controller;

import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.apispec.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiResponseTestController {

    @GetMapping("/test/success")
    public ResponseEntity<CommonResponse<String>> success() {
        return ResponseEntity.ok(new CommonResponse<>("test"));
    }

    @GetMapping("/test/error")
    public ResponseEntity<ErrorResponse> error() {
        return ErrorResponse.error(new CustomException(ErrorCode.NOT_SUPPORTED_HTTP_METHOD));
    }
}
