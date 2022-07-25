package com.infomansion.server.domain.Room.repository;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByUser(User user);
}
