package com.infomansion.server.domain.notification.repository;

import com.infomansion.server.domain.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n FROM Notification n WHERE n.toUsername = :username AND n.isRead = false ORDER BY n.createdDate DESC")
    List<Notification> findSimpleUnReadNotificationsByUsername(@Param("username") String username);

    List<Notification> findNotificationsByToUsername(String toUsername, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Notification n SET n.isRead = true WHERE n.toUsername = :username AND n.isRead = false")
    int updateUnReadNotificationStatus(@Param("username") String username);
}
