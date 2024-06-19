package com.infomansion.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TempPostImageUploadResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String imgUrl;

}
