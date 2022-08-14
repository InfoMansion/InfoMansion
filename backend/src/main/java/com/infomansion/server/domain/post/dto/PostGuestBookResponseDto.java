package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostGuestBookResponseDto {

    private Long id;
    private String content;
    private LocalDateTime modifiedDate;
    private Long likes;

    public PostGuestBookResponseDto(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.modifiedDate = post.getModifiedDate();
        this.likes = (long) post.getUserLikePostList().size();
    }

    @Builder
    public PostGuestBookResponseDto(Long id, String content, LocalDateTime modifiedDate, Long likes) {
        this.id = id;
        this.content = content;
        this.modifiedDate = modifiedDate;
        this.likes = likes;
    }

    public static PostGuestBookResponseDto toDto(Post post) {
        return PostGuestBookResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .modifiedDate(post.getModifiedDate())
                .likes((long) post.getUserLikePostList().size())
                .build();
    }
}
