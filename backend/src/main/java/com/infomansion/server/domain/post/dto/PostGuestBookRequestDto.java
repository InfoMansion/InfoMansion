package com.infomansion.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
public class PostGuestBookRequestDto {

    @NotBlank
    private String content;

}
