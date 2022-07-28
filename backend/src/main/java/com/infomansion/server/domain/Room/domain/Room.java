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
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String roomImg;

    private boolean deleteFlag;

    @Builder
    public Room(Long id, User user, String roomImg) {
        this.id = id;
        this.user = user;
        this.roomImg = roomImg;
        this.deleteFlag = false;
    }

    public void deleteRoom(){
        this.deleteFlag = true;
    }
}