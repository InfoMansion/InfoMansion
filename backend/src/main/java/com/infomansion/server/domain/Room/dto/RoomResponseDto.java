package com.infomansion.server.domain.Room.dto;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RoomResponseDto {

    private Long id;
    private UserResponseDto userResponseDto;
    private String roomImg;


    public RoomResponseDto(Room room){
        this.id = room.getId();
        this.userResponseDto = new UserResponseDto(room.getUser());
        this.roomImg = room.getRoomImg();
    }

}

