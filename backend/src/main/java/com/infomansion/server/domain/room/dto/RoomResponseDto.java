package com.infomansion.server.domain.room.dto;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
public class RoomResponseDto {

    private Long id;
    private User user;
    private String room_img;


    public RoomResponseDto(Room room){
        this.id = room.getId();
        this.user = room.getUser();
        this.room_img = room.getRoom_img();
    }

}
