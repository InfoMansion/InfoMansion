package com.infomansion.server.domain.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class PostbyUserStuffResponseDto {

    private Slice<PostSimpleResponseDto> postsByUserStuff;

    public PostbyUserStuffResponseDto(Slice<PostSimpleResponseDto> postsByUserStuff){
        this.postsByUserStuff = postsByUserStuff;
    }
}
