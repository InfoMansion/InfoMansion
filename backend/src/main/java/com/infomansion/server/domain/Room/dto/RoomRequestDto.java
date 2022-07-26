package com.infomansion.server.domain.Room.dto;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class RoomRequestDto {

    @NotBlank
    private Long userId;
    @NotBlank
    private String roomImg;

    @Builder
    public RoomRequestDto(Long userId) {
        this.userId = userId;
        this.roomImg = "default";
    }

    public Room toEntity(User user){
        return Room.builder()
                .user(user)
                .roomImg(roomImg)
                .build();
    }
}

