package com.infomansion.server.domain.userstuff.domain;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum DefaultStuff {

    WALL(514L, "", Category.NONE, true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
    FLOOR(202L, "", Category.NONE, true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
    TABLE(469L, "나의 하루", Category.DAILY, true, BigDecimal.valueOf(3), BigDecimal.ZERO, BigDecimal.valueOf(2.7), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(5.78)),
    CHAIR(45L, "나의 휴식처", Category.NONE, true,  BigDecimal.valueOf(2), BigDecimal.ZERO, BigDecimal.valueOf(2), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(6.08)),
    GUESTBOOK(265L, "방명록", Category.GUESTBOOK, true,  BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
    POSTBOX(542L, "정리함", Category.POSTBOX, true,  BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);


    private final Long stuffId;
    private final String alias;
    private final Category category;
    private final boolean selected;
    private final BigDecimal posX;
    private final BigDecimal posY;
    private final BigDecimal posZ;
    private final BigDecimal rotX;
    private final BigDecimal rotY;
    private final BigDecimal rotZ;

    public static List<Long> getDefaultStuffIds() {
        return Arrays.stream(values()).map(DefaultStuff::getStuffId).collect(Collectors.toList());
    }

    public static UserStuff getDefaultUserStuff(Stuff stuff, User user) {
        DefaultStuff matchedStuff = valueOfStuffId(stuff.getId());
        return UserStuff.builder()
                .user(user).stuff(stuff).selected(true)
                .alias(matchedStuff.getAlias()).category(matchedStuff.getCategory())
                .posX(matchedStuff.getPosX()).posY(matchedStuff.getPosY()).posZ(matchedStuff.getPosZ())
                .rotX(matchedStuff.getRotX()).rotY(matchedStuff.getRotY()).rotZ(matchedStuff.getRotZ())
                .build();
    }

    private static DefaultStuff valueOfStuffId(Long stuffId) {
        if(stuffId.equals(514L)) return WALL;
        else if(stuffId.equals(202L)) return FLOOR;
        else if(stuffId.equals(469L)) return TABLE;
        else if(stuffId.equals(45L)) return CHAIR;
        else if(stuffId.equals(346L)) return POSTBOX;
        else return GUESTBOOK;
    }

}
