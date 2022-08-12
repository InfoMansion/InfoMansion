package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TempPostSaveResponseDto {
    private Long postId;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;

    @Builder
    public TempPostSaveResponseDto(Long postId, LocalDateTime modifiedDate, String title, String content) {
        this.postId = postId;
        this.modifiedDate = modifiedDate;
        this.title = title;
        this.content = content;
    }

    public static TempPostSaveResponseDto toDto(Post post) {
        return TempPostSaveResponseDto.builder()
                .postId(post.getId())
                .modifiedDate(post.getModifiedDate())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
