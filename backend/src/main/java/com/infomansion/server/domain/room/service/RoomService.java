package com.infomansion.server.domain.room.service;


import com.infomansion.server.domain.room.dto.RoomRecommendResponseDto;
import com.infomansion.server.domain.room.dto.RoomResponseDto;
import com.infomansion.server.domain.room.dto.RoomUserRecommendResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {

    //Room 정보 반환
    RoomResponseDto findRoombyId(Long userId);

    //Room 전체 반환
    List<RoomResponseDto> findAllRoom();

    //Room 삭제
    void deleteRoom(Long roomId);

    //UserId, UserLikePost를 통해 추천된 RoomImg와 UserName 반환
    RoomRecommendResponseDto findRecommendRoomByUserLikePost();

    RoomUserRecommendResponseDto findRecommendRoomByFollowingUsers(Pageable pageable);

    boolean editRoomImg(MultipartFile roomImage);

}
