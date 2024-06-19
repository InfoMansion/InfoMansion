package com.infomansion.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostSaveResponseDto {
    private Long userStuffId;
    private Long postId;
    private String title;
    private String content;

}
