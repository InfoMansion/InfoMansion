package com.infomansion.server.domain.userstuff.api;

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

    @GetMapping("/api/v1/userstuffs")
    private ResponseEntity<CommonResponse<UserStuffResponseDto>> findUserStuffById(@Valid @RequestParam UserStuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.findUserStuffById(requestDto)));
    }

    @GetMapping("/api/v1/userstuffs/{userId}")
    private ResponseEntity<CommonResponse<List<UserStuffResponseDto>>> findAllUserStuff(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommonResponse<>(userStuffService.findAllUserStuff(userId)));
    }
}
