package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class TempPostSaveResponseDto {
    private Long postId;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;

    public static TempPostSaveResponseDto toDto(Post post) {
        return TempPostSaveResponseDto.builder()
                .postId(post.getId())
                .modifiedDate(post.getModifiedDate())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
