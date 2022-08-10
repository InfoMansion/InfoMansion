package com.infomansion.server.domain.room.domain;

import com.infomansion.server.domain.base.BaseTimeEntity;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Getter
@NoArgsConstructor
@Where(clause = "delete_flag = false")
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

    public void changeRoomImage(S3Uploader s3Uploader, MultipartFile roomImage) throws IOException {
        this.roomImg = s3Uploader.uploadFiles(roomImage, "room");
    }

    public static Room createRoom(User user){
        return Room.builder()
                .user(user)
                .build();
    }
}