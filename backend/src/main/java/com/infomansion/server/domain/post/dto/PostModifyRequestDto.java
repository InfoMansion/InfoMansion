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
public class PostModifyRequestDto {

    @NotNull
    private Long postId;

    @NotNull
    private Long userStuffId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<String> images = new ArrayList<>();

    @Builder
    public PostModifyRequestDto(Long postId, Long userStuffId, String title, String content, List<String> images) {
        this.postId = postId;
        this.userStuffId = userStuffId;
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
