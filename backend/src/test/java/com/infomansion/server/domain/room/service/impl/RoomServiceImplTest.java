package com.infomansion.server.domain.room.service.impl;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.Room.dto.RoomRequestDto;
import com.infomansion.server.domain.Room.dto.RoomResponseDto;
import com.infomansion.server.domain.Room.repository.RoomRepository;
import com.infomansion.server.domain.Room.service.RoomService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.security.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomServiceImplTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @MockBean
    private SecurityUtil securityUtil;

    private Long userId;
    private User user;

    @BeforeEach
    public void setUp() {

        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build());

    }

    @AfterEach
    public void cleanUp(){
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("권한이 없는 사람은 room 생성 실패")
    @Test
    public void room_생성_실패(){
        //given
        userRepository.save(user);
        userId = user.getId();

        RoomRequestDto requestDto = RoomRequestDto.builder()
                .userId(userId)
                .build();

        //when
        Throwable exception = assertThrows(CustomException.class, () -> {
            Long roomId = roomService.createRoom(requestDto);
            RoomResponseDto responseDto = roomService.findRoombyId(userId);
        });

        //then
        System.out.println(exception.toString()); //CustomException.
        List<RoomResponseDto> roomList = roomService.findAllRoom();

        assertThat(roomList.size()).isEqualTo(0);
    }


    @DisplayName("권한이 있는 사람은 room 생성 성공")
    @Test
    public void room_생성_조회_성공(){
        //given
        user.grantFromTempToUser();
        userRepository.save(user);
        userId = user.getId();

        RoomRequestDto requestDto = RoomRequestDto.builder()
                .userId(userId)
                .build();

        //when
        Long roomId = roomService.createRoom(requestDto);

        //then
        RoomResponseDto responseDto = roomService.findRoombyId(userId);
        assertThat(responseDto.getUserResponseDto().getUsername()).isEqualTo("infomansion");
    }


    @DisplayName("room 삭제 성공")
    @Test
    public void room_삭제(){
        //given
        user.grantFromTempToUser();
        userRepository.save(user);
        userId = user.getId();

        RoomRequestDto requestDto = RoomRequestDto.builder()
                .userId(userId)
                .build();
        Long roomId = roomService.createRoom(requestDto);

        //when
        roomService.deleteRoom(roomId);

        //then
        Optional<Room> findRoom = roomRepository.findById(roomId);
        assertThat(findRoom.get().isDeleteFlag()).isEqualTo(true);
    }
}
