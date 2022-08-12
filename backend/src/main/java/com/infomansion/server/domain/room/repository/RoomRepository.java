package com.infomansion.server.domain.room.repository;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByUser(User user);

    @Query("select r from Room r join fetch r.user where r.user.id = :userId")
    Optional<Room> findRoomWithUser(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM Room ORDER BY rand() LIMIT 20", nativeQuery = true)
    List<Room> findRandomRoom();
}
