package com.infomansion.server.domain.room.service;

import com.infomansion.server.domain.room.dto.RoomRequestDto;
import com.infomansion.server.domain.room.dto.RoomResponseDto;

import java.util.List;

public interface RoomService {

    //Room 생성
    Long createRoom(RoomRequestDto requestDto);

    //Room 정보 반환
    RoomResponseDto findRoombyId(RoomRequestDto requestDto);

    //Room 전체 반환
    List<RoomResponseDto> findAllRoom();


    //Room 삭제
    void deleteRoom(Long user_id);

}
