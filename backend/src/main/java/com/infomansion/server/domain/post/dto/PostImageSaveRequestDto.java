package com.infomansion.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class PostImageSaveRequestDto {

    @NotBlank
    private Long userStuffId;

    private Long postId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
