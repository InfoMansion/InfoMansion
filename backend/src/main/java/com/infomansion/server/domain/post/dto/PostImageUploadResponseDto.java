package com.infomansion.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostImageUploadResponseDto {
    private Long userStuffId;
    private Long postId;
    private String title;
    private String content;
    private String imgUrl;

    @Builder
    public PostImageUploadResponseDto(Long userStuffId, Long postId, String title, String content, String imgUrl) {
        this.userStuffId = userStuffId;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }
}
