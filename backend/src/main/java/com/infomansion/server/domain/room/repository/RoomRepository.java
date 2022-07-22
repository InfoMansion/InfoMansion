package com.infomansion.server.domain.room.repository;

import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r join fetch r.user")
    public Room findByUser(User user);
}
