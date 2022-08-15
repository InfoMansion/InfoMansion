package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostGuestBookResponseDto {

    private Long id;
    private String username;
    private String content;
    private LocalDateTime modifiedDate;

    @Builder
    public PostGuestBookResponseDto(Long id, String username, String content, LocalDateTime modifiedDate) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.modifiedDate = modifiedDate;
    }

    public static PostGuestBookResponseDto toDto(Post post) {
        return PostGuestBookResponseDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .content(post.getContent())
                .modifiedDate(post.getModifiedDate())
                .build();
    }
}
