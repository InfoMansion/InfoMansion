package com.infomansion.server.domain.room.repository;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.Room.repository.RoomRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void cleanUp(){
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Room 생성 및 조회 성공")
    @Test
    public void Room_생성_및_조회(){

        User user = null;
        // User 생성
        for(int i=0;i<5;i++){
            String email = "infomansion@test.com" + (i+1);
            String password = "testPassword1$" + (i+1);
            String tel = "01012345678" + (i+1);
            String username = "infomansion" + (i+1);
            String categories = "IT,COOK";

            user = userRepository.save(User.builder()
                    .email(email)
                    .password(password)
                    .tel(tel)
                    .username(username)
                    .categories(categories)
                    .build());

            Long userId = user.getId();

            //Room 생성
            String roomImg = "info/roomImg" + (i+1);

            Room room = Room.builder()
                    .user(user)
                    .build();

            roomRepository.save(room);
        }

        Room room1 = roomRepository.findByUser(user).get();
        assertThat(room1.getUser().getUsername()).isEqualTo("infomansion5");

    }

}
