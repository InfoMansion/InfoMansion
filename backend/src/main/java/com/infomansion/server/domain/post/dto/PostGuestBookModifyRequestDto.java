package com.infomansion.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostGuestBookModifyRequestDto {

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    @Builder
    public PostGuestBookModifyRequestDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
