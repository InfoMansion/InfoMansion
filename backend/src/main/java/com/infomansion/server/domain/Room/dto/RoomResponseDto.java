package com.infomansion.server.domain.Room.dto;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class RoomResponseDto {

    private Long id;
    private UserResponseDto userResponseDto;
    private String roomImg;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;


    public RoomResponseDto(Room room){
        this.id = room.getId();
        this.userResponseDto = new UserResponseDto(room.getUser());
        this.roomImg = room.getRoomImg();
        this.createdTime = room.getCreatedDate();
        this.modifiedTime = room.getModifiedDate();
    }

}

