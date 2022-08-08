package com.infomansion.server.domain.notification.service;

import com.infomansion.server.domain.notification.dto.NotificationResponseDto;
import com.infomansion.server.domain.notification.dto.UnReadNotificationResponseDto;

import java.util.List;

public interface NotificationService {
    List<UnReadNotificationResponseDto> findSimpleUnReadNotifications();
    List<NotificationResponseDto> findAllNotificationsPagable(Integer pageNum);
    int readUnReadNotifications();
}
