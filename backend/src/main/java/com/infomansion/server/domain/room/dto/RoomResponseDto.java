package com.infomansion.server.domain.room.dto;

import com.infomansion.server.domain.room.domain.Room;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RoomResponseDto {

    private String userName;
    private String roomImg;


    public RoomResponseDto(Room room){
        this.userName = room.getUser().getUsername();
        this.roomImg = room.getRoomImg();
    }

}

