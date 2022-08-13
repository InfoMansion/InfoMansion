package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.dto.UserAuthRequestDto;
import com.infomansion.server.domain.user.dto.UserChangePasswordDto;
import com.infomansion.server.domain.user.dto.UserModifyProfileRequestDto;
import com.infomansion.server.domain.user.dto.UserSearchResponseDto;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/users/password")
    public ResponseEntity<? extends BasicResponse> userAuthBeforeChangePassword(@Valid @RequestBody UserAuthRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.authBeforeChangePassword(requestDto)));
    }

    @PatchMapping("/api/v1/users/password")
    public ResponseEntity<? extends BasicResponse> userChangePassword(@Valid @RequestBody UserChangePasswordDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.changePasswordAfterAuth(requestDto)));
    }

    @GetMapping("/api/v1/users/{username}")
    public ResponseEntity<? extends BasicResponse> findUserByUsername(@Valid @PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findByUsername(username)));
    }

    @GetMapping("/api/v1/users/info/simple")
    public ResponseEntity<? extends BasicResponse> getUserSimpleProfile() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findSimpleProfile()));
    }

    @PostMapping(value = "/api/v1/users/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<? extends BasicResponse> modifyUserProfile(@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,@RequestPart(value = "profileInfo") UserModifyProfileRequestDto profileInfo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.modifyUserProfile(profileImage, profileInfo)));
    }

    @GetMapping("api/v1/users/search/username")
    public ResponseEntity<CommonResponse<UserSearchResponseDto>> searchUserByUsername(@Valid @RequestParam String searchWord, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findUserBySearchWordForUserName(searchWord, pageable)));
    }

    @PatchMapping("api/v1/users/changestate")
    public ResponseEntity<CommonResponse<Boolean>> changeUserState(@Valid @RequestParam String username){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.changeUserState(username)));
    }

    @PatchMapping("api/v1/users/resign")
    public ResponseEntity<CommonResponse<Long>> deleteUser(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.deleteUser()));
    }

    @GetMapping("/api/v1/users/credit")
    public ResponseEntity<? extends BasicResponse> getUserCredit() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findUserCredit()));
    }
}
