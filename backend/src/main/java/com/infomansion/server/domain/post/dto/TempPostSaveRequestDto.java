package com.infomansion.server.domain.post.dto;

import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Getter
@Value
public class TempPostSaveRequestDto {

    @NotBlank
    String title;
    @NotBlank
    String content;

    public TempPostSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
