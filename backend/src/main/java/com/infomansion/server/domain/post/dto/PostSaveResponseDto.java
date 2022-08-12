package com.infomansion.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSaveResponseDto {
    private Long userStuffId;
    private Long postId;
    private String title;
    private String content;

    @Builder
    public PostSaveResponseDto(Long userStuffId, Long postId, String title, String content) {
        this.userStuffId = userStuffId;
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
