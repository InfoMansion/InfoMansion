package com.infomansion.server.domain.Room.domain;

import com.infomansion.server.domain.base.BaseTimeEntity;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String roomImg;

    private boolean deleteFlag;

    @Builder
    public Room(User user) {
        this.user = user;
        this.roomImg = "default";
        this.deleteFlag = false;
    }

    public void deleteRoom(){
        this.deleteFlag = true;
    }

    public static Room createRoom(User user){
        return Room.builder()
                .user(user)
                .build();
    }
}