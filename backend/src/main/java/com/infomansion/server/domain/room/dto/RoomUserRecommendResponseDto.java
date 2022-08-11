package com.infomansion.server.domain.room.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class RoomUserRecommendResponseDto {
    private Slice<RoomResponseDto> roomResponseDtos;

    public RoomUserRecommendResponseDto(Slice<RoomResponseDto> roomResponseDtos){
        this.roomResponseDtos = roomResponseDtos;
    }
}
