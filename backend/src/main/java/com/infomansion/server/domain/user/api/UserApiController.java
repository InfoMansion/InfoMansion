package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<CommonResponse<Long>> userSignUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userService.join(requestDto)));
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<CommonResponse<TokenDto>> userLogin(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.login(requestDto)));
    }

    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<? extends BasicResponse> userReissue(@Valid @RequestBody ReissueDto reissueDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.reissue(reissueDto)));
    }

    @GetMapping("/api/v1/users/password")
    public ResponseEntity<? extends BasicResponse> userAuthBeforeChangePassword(@Valid @RequestBody UserAuthRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.authBeforeChangePassword(requestDto)));
    }

    @PatchMapping("/api/v1/users/password")
    public ResponseEntity<? extends BasicResponse> userChangePassword(@Valid @RequestBody UserChangePasswordDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.changePasswordAfterAuth(requestDto)));
    }

    @PatchMapping("/api/v1/users/categories")
    public ResponseEntity<? extends BasicResponse> userChangeCategories(@Valid @RequestBody UserChangeCategoriesDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.changeCategories(requestDto)));
    }
}
