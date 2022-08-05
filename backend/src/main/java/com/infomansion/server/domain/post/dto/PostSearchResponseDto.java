package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class PostSearchResponseDto {

    private Slice<UserSimpleProfileResponseDto> postsByUserName;
    private Slice<PostSimpleResponseDto> postsByTitle;
    private Slice<PostSimpleResponseDto> postsByContent;

    public PostSearchResponseDto(Slice<UserSimpleProfileResponseDto> postsByUserName,
                                 Slice<PostSimpleResponseDto> postsByTitle,
                                 Slice<PostSimpleResponseDto> postsByContent){
        this.postsByUserName = postsByUserName;
        this.postsByTitle = postsByTitle;
        this.postsByContent = postsByContent;
    }
}
