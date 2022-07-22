package com.infomansion.server.domain.room.domain;

import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Room {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String room_img;

    @Builder
    public Room(Long id, User user, String room_img) {
        this.id = id;
        this.user = user;
        this.room_img = room_img;
    }
}
