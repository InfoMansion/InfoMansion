package com.infomansion.server.domain.userstuff.api;

import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffModifyRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffResponseDto;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserStuffApiController {

    private UserStuffService userStuffService;

    @PostMapping("/api/v1/userstuffs")
    private ResponseEntity<CommonResponse<Long>> saveUserStuff(@Valid @RequestBody UserStuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userStuffService.saveUserStuff(requestDto)));
    }

    @GetMapping("/api/v1/userstuffs/{userStuffId}")
    private ResponseEntity<CommonResponse<UserStuffResponseDto>> findUserStuffById(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.findUserStuffByUserStuffId(userStuffId)));
    }

    @GetMapping("/api/v1/userstuffs/list/{userId}")
    private ResponseEntity<CommonResponse<List<UserStuffResponseDto>>> findAllUserStuff(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.findAllUserStuff(userId)));
    }

    @PutMapping("/api/v1/userstuffs/{userStuffId}")
    private ResponseEntity<CommonResponse<Long>> excludeUserStuff(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.excludeUserStuff(userStuffId)));
    }

    @PutMapping("/api/v1/userstuffs")
    private ResponseEntity<CommonResponse<Long>> includeUserStuff(@Valid @RequestBody UserStuffIncludeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.includeUserStuff(requestDto)));
    }

    @PutMapping("/api/v1/userstuffs/option")
    private ResponseEntity<CommonResponse<Long>> modifyAliasAndCategory(@Valid @RequestBody UserStuffModifyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.modifyAliasAndCategory(requestDto)));
    }
}
