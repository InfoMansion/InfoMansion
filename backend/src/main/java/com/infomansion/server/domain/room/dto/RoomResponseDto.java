package com.infomansion.server.domain.room.dto;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RoomResponseDto {

    private Long id;
    private User user;
    private String roomImg;


    public RoomResponseDto(Room room){
        this.id = room.getId();
        this.user = room.getUser();
        this.roomImg = room.getRoomImg();
    }

}
