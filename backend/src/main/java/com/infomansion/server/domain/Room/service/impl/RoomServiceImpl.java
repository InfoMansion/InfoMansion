package com.infomansion.server.domain.Room.service.impl;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.Room.dto.RoomRequestDto;
import com.infomansion.server.domain.Room.dto.RoomResponseDto;
import com.infomansion.server.domain.Room.repository.RoomRepository;
import com.infomansion.server.domain.Room.service.RoomService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.domain.UserAuthority;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Long createRoom(RoomRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(user.getAuthority().equals(UserAuthority.ROLE_TEMP)){
            throw new CustomException(ErrorCode.NOT_VALID_USER);
        }

        Room room = requestDto.toEntity(user);
        return roomRepository.save(room).getId();
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        room.deleteRoom();
    }


    @Override
    public RoomResponseDto findRoombyId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        return new RoomResponseDto(roomRepository.findByUser(user).get());

    }

    @Override
    public List<RoomResponseDto> findAllRoom() {
        List<RoomResponseDto> responseDtoList = new ArrayList<>();

        roomRepository.findAll()
                .forEach(room -> responseDtoList.add(
                        new RoomResponseDto(room)
                ));
        return responseDtoList;
    }

}
