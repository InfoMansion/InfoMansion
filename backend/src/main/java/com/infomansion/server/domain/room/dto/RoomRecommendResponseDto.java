package com.infomansion.server.domain.room.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RoomRecommendResponseDto {

    private List<RoomResponseDto> roomResponseDtos;

    public RoomRecommendResponseDto(List<RoomResponseDto> roomResponseDtos){
        this.roomResponseDtos = roomResponseDtos;
    }

}