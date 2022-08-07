package com.infomansion.server.domain.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class PostSearchResponseDto {

    private Slice<PostSimpleResponseDto> postsByTitleOrContent;

    public PostSearchResponseDto(Slice<PostSimpleResponseDto> postsByTitleOrContent){
        this.postsByTitleOrContent = postsByTitleOrContent;
    }
}
