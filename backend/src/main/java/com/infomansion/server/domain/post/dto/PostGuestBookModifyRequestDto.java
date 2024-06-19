package com.infomansion.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class PostGuestBookModifyRequestDto {

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

}
