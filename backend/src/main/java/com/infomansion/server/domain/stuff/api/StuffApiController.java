package com.infomansion.server.domain.stuff.api;

import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.service.StuffService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StuffApiController {

    private final StuffService stuffService;

    @PostMapping("/api/v1/stuffs")
    public ResponseEntity<CommonResponse<Long>> createStuff(@Valid @RequestBody StuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(stuffService.createStuff(requestDto)));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/api/v1/stuffs")
    public ResponseEntity<CommonResponse<List<StuffResponseDto>>> findAllStuff() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(stuffService.findAllStuff()));
    }

    @GetMapping("/api/v1/stuffs/{stuffId}")
    public ResponseEntity<CommonResponse<StuffResponseDto>> findStuffById(@PathVariable Long stuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(stuffService.findStuffById(stuffId)));
    }

    @PutMapping("/api/v1/stuffs/{stuffId}")
    public ResponseEntity<CommonResponse<Long>> updateStuff(@PathVariable Long stuffId, @Valid @RequestBody StuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(stuffService.updateStuff(stuffId, requestDto)));
    }

    @PatchMapping("/api/v1/stuffs/{stuffId}")
    public ResponseEntity<CommonResponse<Long>> removeStuff(@Valid @PathVariable Long stuffId) {
        stuffService.removeStuff(stuffId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(stuffId));
    }

}
