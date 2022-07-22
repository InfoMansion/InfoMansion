package com.infomansion.server.domain.room.dto;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class RoomRequestDto {

    @NotBlank
    private Long userId;
    @NotBlank
    private String room_img;

    @Builder
    public RoomRequestDto(Long userId) {
        this.userId = userId;
        this.room_img = "default";
    }

    public Room toEntity(User user){
        return Room.builder()
                .user(user)
                .room_img(room_img)
                .build();
    }
}
