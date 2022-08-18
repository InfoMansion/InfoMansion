package com.infomansion.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostGuestBookRequestDto {

    @NotBlank
    private String content;

    @Builder
    public PostGuestBookRequestDto(String content) {
        this.content = content;
    }

}
