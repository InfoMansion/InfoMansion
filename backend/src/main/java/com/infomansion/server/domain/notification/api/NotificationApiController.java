package com.infomansion.server.domain.notification.api;

import com.infomansion.server.domain.notification.service.NotificationService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/api/v1/notifications")
    public ResponseEntity<? extends BasicResponse> getUnReadNotifications() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(notificationService.findSimpleUnReadNotifications()));
    }

    @GetMapping("/api/v1/notifications/all")
    public ResponseEntity<? extends BasicResponse> getAllNotifications(@Valid @RequestParam Integer pageNum) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(notificationService.findAllNotificationsPagable(pageNum)));
    }

    @PostMapping("/api/v1/notifications")
    public ResponseEntity<? extends BasicResponse> readUnReadNotifications() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(notificationService.readUnReadNotifications()));
    }

}
