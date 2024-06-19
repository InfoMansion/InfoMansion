package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostGuestBookResponseDto {

    private Long id;
    private String username;
    private String content;
    private LocalDateTime modifiedDate;

    public static PostGuestBookResponseDto toDto(Post post) {
        return PostGuestBookResponseDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .content(post.getContent())
                .modifiedDate(post.getModifiedDate())
                .build();
    }
}
