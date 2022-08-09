package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FollowApiController {

    private final UserService userService;

    @PostMapping("/api/v1/follow/{username}")
    public ResponseEntity<? extends BasicResponse> followUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.followUser(username)));
    }

    @DeleteMapping("/api/v1/follow/{username}")
    public ResponseEntity<? extends BasicResponse> unFollowUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.unFollowUser(username)));
    }

    @GetMapping("/api/v1/follow/follower/{username}")
    public ResponseEntity<? extends BasicResponse> findFollowerUserList(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findFollowerUserList(username)));
    }

    @GetMapping("/api/v1/follow/following/{username}")
    public ResponseEntity<? extends BasicResponse> findFollowingrUserList(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.findFollowingUserList(username)));
    }
}