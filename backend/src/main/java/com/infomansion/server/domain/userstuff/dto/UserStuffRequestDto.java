package com.infomansion.server.domain.userstuff.dto;


import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;

@Transactional
@Getter
@NoArgsConstructor
public class UserStuffRequestDto {

    @NotBlank
    private Long userId;

    @NotBlank
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
                .posX(0f).posY(0f).posZ(0f)
                .rotX(0f).rotY(0f).rotZ(0f)
                .build();
    }


}
