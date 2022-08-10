package com.infomansion.server.domain.room.service;


import com.infomansion.server.domain.room.dto.RoomRecommendResponseDto;
import com.infomansion.server.domain.room.dto.RoomResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {

    //Room 정보 반환
    RoomResponseDto findRoombyId(Long userId);

    //Room 전체 반환
    List<RoomResponseDto> findAllRoom();

    //Room 삭제
    void deleteRoom(Long roomId);

    //UserId를 통해 추천된 RoomImg와 UserName 반환
    RoomRecommendResponseDto findRecommendRoom();

    //UserId, UserLikePost를 통해 추천된 RoomImg와 UserName 반환
    RoomRecommendResponseDto findRecommendRoomByUserLikePost();

    boolean editRoomImg(MultipartFile roomImage);

}
