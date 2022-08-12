package com.infomansion.server.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TempPostSaveRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public TempPostSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
