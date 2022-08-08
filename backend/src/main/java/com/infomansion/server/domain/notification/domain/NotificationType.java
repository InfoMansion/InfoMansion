package com.infomansion.server.domain.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    FOLLOW_USER("FU"),
    LIKE_POST("LP"),
    GUEST_BOOK("GB");

    private final String ntype;


}
