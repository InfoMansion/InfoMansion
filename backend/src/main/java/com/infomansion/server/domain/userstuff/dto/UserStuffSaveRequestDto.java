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
public class UserStuffSaveRequestDto {

    @NotNull
    private Long stuffId;

    @Builder
    public UserStuffSaveRequestDto(Long stuffId) {
        this.stuffId = stuffId;
    }

    public UserStuff toEntity(User user, Stuff stuff) {
        return UserStuff.builder()
                .stuff(stuff)
                .user(user)
                .alias(stuff.getStuffNameKor())
                .selected(false)
                .posX(BigDecimal.ZERO).posY(BigDecimal.ZERO).posZ(BigDecimal.ZERO)
                .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO)
                .build();
    }

}
