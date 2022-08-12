package com.infomansion.server.domain.room.api;

import com.infomansion.server.domain.room.dto.RoomResponseDto;
import com.infomansion.server.domain.room.dto.RoomUserRecommendResponseDto;
import com.infomansion.server.domain.room.service.RoomService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class RoomApiController {
    private final RoomService roomService;

    @GetMapping("/api/v1/rooms/{userId}")
    private ResponseEntity<CommonResponse<RoomResponseDto>> findRoomById(@Valid @PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRoombyId(userId)));
    }

    @GetMapping("/api/v1/rooms/myroom")
    private ResponseEntity<CommonResponse<RoomResponseDto>> findMyRoom(){
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRoombyId(userId)));
    }


    @PatchMapping("/api/v1/rooms/{roomId}")
    private ResponseEntity<CommonResponse<Long>> deleteRoom(@Valid @PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomId));
    }

    @GetMapping("/api/v2/rooms/recommend")
    private ResponseEntity<CommonResponse<RoomUserRecommendResponseDto>> findRecommendRoomByUserLikePost(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRecommendRoomByUserLikePost(pageable)));
    }

    @PutMapping(value = "/api/v1/rooms/edit", consumes = {"multipart/form-data"})
    public ResponseEntity<? extends BasicResponse> editRoom(@RequestParam(value = "roomImg")MultipartFile roomImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(roomService.editRoomImg(roomImage)));
    }

    @GetMapping("/api/v1/rooms/random")
    public ResponseEntity<? extends BasicResponse> findRandomRoomImage() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(roomService.findRandomRoomImage()));
    }
}
