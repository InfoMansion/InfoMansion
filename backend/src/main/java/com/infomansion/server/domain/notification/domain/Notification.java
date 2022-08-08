package com.infomansion.server.domain.notification.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "read")
    private boolean isRead;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private String fromUsername;

    private String toUsername;

    private Long targetId;

    @Column(name = "ntype", length = 2)
    private String notificationType;

    @Builder
    public Notification(boolean isRead, String fromUsername, String toUsername, NotificationType notificationType, Long targetId) {
        this.isRead = isRead;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.notificationType = notificationType.getNtype();
        this.targetId = targetId;
    }

    public static Notification createNotification(String fromUsername, String toUsername, NotificationType notiType, Long targetId) {
        return Notification.builder()
                .isRead(false)
                .fromUsername(fromUsername)
                .toUsername(toUsername)
                .notificationType(notiType)
                .targetId(targetId)
                .build();
    }

    public void readNotification() {
        this.isRead = true;
    }
}
