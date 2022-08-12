package com.infomansion.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TempPostImageUploadResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String imgUrl;

    @Builder
    public TempPostImageUploadResponseDto(Long postId, String title, String content, String imgUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }
}
