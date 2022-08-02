package com.infomansion.server.domain.stuff.domain;

import com.infomansion.server.global.util.common.EnumMapperType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StuffType implements EnumMapperType {
    GEUSTBOOK("방명록"),
    WALL("벽지"),
    FLOOR("바닥"),
    DESK("책상"),
    TABLE("테이블"),
    CHAIR("의자"),
    SOFA("소파"),
    PLANT("화분"),
    WORKOUT("운동기구"),
    CLOSET("옷장"),
    HANGER("행거"),
    TVDAI("DAI"),
    DOCUMENT("문서"),
    SHELF("선반"),
    DRAWER("서랍장"),
    INSTRUMENTS("악기"),
    CARPET("카펫/러그"),
    TABLEWARE("식기류"),
    MONITOR("모니터"),
    BED("침대"),
    KITCHEN("주방용품"),
    BOOK("책"),
    FRAME("액자"),
    LIGHT("전등"),
    OTHER("기타");

    private final String stuffTypeName;

    @Override
    public String getEnum() {
        return this.name();
    }

    @Override
    public String getEnumName() {
        return this.stuffTypeName;
    }
}
