package com.infomansion.server.domain.Room.service;


import com.infomansion.server.domain.Room.dto.RoomRequestDto;
import com.infomansion.server.domain.Room.dto.RoomResponseDto;

import java.util.List;

public interface RoomService {

    //Room 생성
    Long createRoom(RoomRequestDto requestDto);

    //Room 정보 반환
    RoomResponseDto findRoombyId(Long userId);

    //Room 전체 반환
    List<RoomResponseDto> findAllRoom();

    //Room 삭제
    void deleteRoom(Long roomId);

}
