package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
