package com.infomansion.server.domain.notification.service.impl;

import com.infomansion.server.domain.notification.dto.NotificationResponseDto;
import com.infomansion.server.domain.notification.dto.UnReadNotificationResponseDto;
import com.infomansion.server.domain.notification.repository.NotificationRepository;
import com.infomansion.server.domain.notification.service.NotificationService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<UnReadNotificationResponseDto> findSimpleUnReadNotifications() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return notificationRepository.findSimpleUnReadNotificationsByUsername(user.getUsername())
                .stream().map(UnReadNotificationResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> findAllNotificationsPagable(Integer pageNum) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return notificationRepository.findNotificationsByToUsername(user.getUsername(), PageRequest.of(pageNum-1, 10, Sort.by("createdDate").descending()))
                .stream().map(NotificationResponseDto::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int readUnReadNotifications() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return notificationRepository.updateUnReadNotificationStatus(user.getUsername());
    }
}
