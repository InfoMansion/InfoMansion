package com.infomansion.server.domain.user.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class UserSearchResponseDto {
    private Slice<UserSimpleProfileResponseDto> usersByUserName;

    public UserSearchResponseDto(Slice<UserSimpleProfileResponseDto> usersByUserName){
        this.usersByUserName = usersByUserName;
    }
}
