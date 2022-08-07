package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostSimpleResponseDto {

    private Long id;
    private String title;
    private String content;
    private String defaultPostThumbnail;
    private LocalDateTime modifiedDate;
    private Long likes;

    public PostSimpleResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.defaultPostThumbnail = post.getDefaultPostThumbnail();
        this.modifiedDate = post.getModifiedDate();
        this.likes = post.getLikesPost().getLikes();
    }

}
