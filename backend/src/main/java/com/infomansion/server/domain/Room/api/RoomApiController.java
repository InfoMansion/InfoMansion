package com.infomansion.server.domain.Room.api;

import com.infomansion.server.domain.Room.dto.RoomRequestDto;
import com.infomansion.server.domain.Room.dto.RoomResponseDto;
import com.infomansion.server.domain.Room.service.RoomService;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.security.SecurityUtil;
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

    @GetMapping("/api/v1/rooms/{userId}")
    private ResponseEntity<CommonResponse<RoomResponseDto>> findRoomById(@Valid @PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRoombyId(userId)));
    }

    @GetMapping("/api/v1/rooms/myroom")
    private ResponseEntity<CommonResponse<RoomResponseDto>> findMyRoom(){
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomService.findRoombyId(userId)));
    }


    @DeleteMapping("/api/v1/rooms/{roomId}")
    private ResponseEntity<CommonResponse<Long>> deleteRoom(@Valid @PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(roomId));
    }
}
