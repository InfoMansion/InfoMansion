package com.infomansion.server.domain.room.repository;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.room.repository.RoomRepository;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RunAs;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Room 생성 및 조회 성공")
    @Test
    public void Room_생성_및_조회(){

        //User 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        User user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build());

        Long user_id = user.getId();

        //Room 생성
        String roomImg = "info/roomImg";

        Room room = Room.builder()
                .user(user)
                .room_img(roomImg).
                build();

        roomRepository.save(room);

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList.get(0).getUser().getId()).isEqualTo(user_id);

    }

}
