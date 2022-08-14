package com.infomansion.server.domain.room.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.room.dto.RoomResponseDto;
import com.infomansion.server.domain.room.dto.RoomUserRecommendResponseDto;
import com.infomansion.server.domain.room.repository.RoomRepository;
import com.infomansion.server.domain.room.service.RoomService;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.FollowRepository;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostService postService;

    private final S3Uploader s3Uploader;

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        room.deleteRoom();
    }

    @Override
    public RoomUserRecommendResponseDto findRecommendRoomByUserLikePost(Pageable pageable) {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String userCategories = user.getCategories();

        List<Category> categories = Arrays.stream(userCategories.split(",")).map(Category::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        Slice<RoomResponseDto> userIds = postRepository.findTop27PostByUserLikePost(user, categories, start, end, pageable)
                .map(userId -> new RoomResponseDto(roomRepository.findRoomWithUser(userId).get()));

        return new RoomUserRecommendResponseDto(userIds);
    }

    @Override
    public RoomUserRecommendResponseDto findRecommendRoomByFollowingUsers(Pageable pageable) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Slice<RoomResponseDto> userIds = followRepository.findFollowingUserRecommend(user, pageable)
                .map(userId -> new RoomResponseDto(roomRepository.findRoomWithUser(userId).get()));

        return new RoomUserRecommendResponseDto(userIds);
    }

    @Transactional
    @Override
    public boolean editRoomImg(MultipartFile roomImage) {
        Room loginUserRoom = roomRepository.findRoomWithUser(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        if(!loginUserRoom.getRoomImg().equals("https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/room/default_room.png"))
            s3Uploader.deleteFiles(Arrays.asList(loginUserRoom.getRoomImg()));
        try {
            loginUserRoom.changeRoomImage(s3Uploader, roomImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<String> findRandomRoomImage() {
        return roomRepository.findRandomRoom().stream().map(Room::getRoomImg)
                .collect(Collectors.toList());
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
