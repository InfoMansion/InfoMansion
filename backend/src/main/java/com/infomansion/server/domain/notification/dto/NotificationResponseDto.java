package com.infomansion.server.domain.notification.dto;

import com.infomansion.server.domain.notification.domain.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NotificationResponseDto {
    private Long id;
    private String fromUsername;
    private Long targetId;
    private String ntype;
    private LocalDateTime createdDate;
    private boolean isRead;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.fromUsername = notification.getFromUsername();
        this.targetId = notification.getTargetId();
        this.ntype = notification.getNotificationType();
        this.createdDate = notification.getCreatedDate();
        this.isRead = notification.isRead();
    }
}
