package com.infomansion.server.domain.room.api;

import com.infomansion.server.domain.room.dto.RoomRequestDto;
import com.infomansion.server.domain.room.dto.RoomResponseDto;
import com.infomansion.server.domain.room.service.RoomService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class RoomApiController {

    private final RoomService roomService;

    @PostMapping("/api/v1/rooms")
    private ResponseEntity<CommonResponse<Long>> createRoom(@Valid @RequestBody RoomRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(roomService.createRoom(requestDto)));
    }

    @GetMapping("/api/v1/rooms")
    private ResponseEntity<CommonResponse<RoomResponseDto>> findRoomById(@Valid @RequestParam RoomRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRoombyId(requestDto)));
    }

    @DeleteMapping("/api/v1/rooms/{userId}")
    private ResponseEntity<CommonResponse<Long>> deleteRoom(@Valid @PathVariable Long userId){
        roomService.deleteRoom(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(userId));
    }
}
