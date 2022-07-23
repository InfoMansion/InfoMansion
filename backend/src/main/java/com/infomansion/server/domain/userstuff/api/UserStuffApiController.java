package com.infomansion.server.domain.userstuff.api;

import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserStuffApiController {

    private final UserStuffService userStuffService;

    @PostMapping("/api/v1/userstuffs")
    public ResponseEntity<CommonResponse<Long>> saveUserStuff(@Valid @RequestBody UserStuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userStuffService.saveUserStuff(requestDto)));
    }

    @GetMapping("/api/v1/userstuffs/{userStuffId}")
    public ResponseEntity<CommonResponse<UserStuffResponseDto>> findUserStuffById(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findUserStuffByUserStuffId(userStuffId)));
    }

    @GetMapping("/api/v1/userstuffs/list/{userId}")
    public ResponseEntity<CommonResponse<List<UserStuffResponseDto>>> findAllUserStuff(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findAllUserStuff(userId)));
    }

    @PutMapping("/api/v1/userstuffs/{userStuffId}")
    public ResponseEntity<CommonResponse<Long>> excludeUserStuff(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.excludeUserStuff(userStuffId)));
    }

    @PutMapping("/api/v1/userstuffs")
    public ResponseEntity<CommonResponse<Long>> includeUserStuff(@Valid @RequestBody UserStuffIncludeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.includeUserStuff(requestDto)));
    }

    @PutMapping("/api/v1/userstuffs/option")
    public ResponseEntity<CommonResponse<Long>> modifyAliasAndCategory(@Valid @RequestBody UserStuffModifyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.modifyAliasAndCategory(requestDto)));
    }

    @PutMapping("/api/v1/userstuffs/position")
    public ResponseEntity<CommonResponse<Long>> modifyPosAndRot(@Valid @RequestBody UserStuffPositionRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.modifyPosAndRot(requestDto)));
    }

    @DeleteMapping("/api/v1/userstuffs/{userStuffId}")
    public ResponseEntity<CommonResponse<Long>> removeUserStuff(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.removeUserStuff(userStuffId)));
    }
}
