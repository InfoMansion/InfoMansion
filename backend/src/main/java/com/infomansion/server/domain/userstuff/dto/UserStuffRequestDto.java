package com.infomansion.server.domain.userstuff.dto;


import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long stuffId;

    @Builder
    public UserStuffRequestDto(Long userId, Long stuffId) {
        this.userId = userId;
        this.stuffId = stuffId;
    }

    public UserStuff toEntity(User user, Stuff stuff) {
        return UserStuff.builder()
                .stuff(stuff)
                .user(user)
                .selected(false)
                .posX(BigDecimal.ZERO).posY(BigDecimal.ZERO).posZ(BigDecimal.ZERO)
                .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO)
                .build();
    }


}
