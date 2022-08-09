package com.infomansion.server.domain.userstuff.api;

import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.apispec.BasicResponse;
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

    @PostMapping("/api/v1/userstuffs/list")
    public ResponseEntity<CommonResponse<Long>> saveUserStuff(@Valid @RequestBody UserStuffSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userStuffService.saveUserStuff(requestDto)));
    }

    @GetMapping("/api/v1/userstuffs/{userStuffId}")
    public ResponseEntity<CommonResponse<UserStuffResponseDto>> findUserStuffById(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findUserStuffByUserStuffId(userStuffId)));
    }

    @GetMapping("/api/v1/userstuffs/list")
    public ResponseEntity<CommonResponse<List<UserStuffEditResponseDto>>> findAllUserStuff() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findAllUserStuff()));
    }

    @PutMapping("/api/v1/userstuffs/option")
    public ResponseEntity<CommonResponse<Long>> modifyAliasAndCategory(@Valid @RequestBody UserStuffModifyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.modifyAliasOrCategory(requestDto)));
    }

    @PatchMapping("/api/v1/userstuffs/{userStuffId}")
    public ResponseEntity<CommonResponse<Long>> removeUserStuff(@PathVariable Long userStuffId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.removeUserStuff(userStuffId)));
    }

    @GetMapping("/api/v1/userstuffs/room/{username}")
    public ResponseEntity<? extends BasicResponse> findArrangedUserStuffByUsername(@Valid @PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findArrangedUserStuffByUsername(username)));
    }

    @PostMapping("/api/v2/user-stuff")
    public ResponseEntity<? extends BasicResponse> purchaseStuff(@Valid @RequestBody UserStuffPurchaseRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userStuffService.purchaseStuff(requestDto)));
    }

    @GetMapping("/api/v1/userstuffs/category")
    public ResponseEntity<? extends BasicResponse> findCategoryPlacedInRoom() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.findCategoryPlacedInRoom()));
    }

    @PutMapping("/api/v1/userstuffs/edit")
    public ResponseEntity<? extends BasicResponse> editUserStuffPlaced(@Valid @RequestBody List<UserStuffEditRequestDto> requestDtos) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userStuffService.editUserStuff(requestDtos)));
    }
}
