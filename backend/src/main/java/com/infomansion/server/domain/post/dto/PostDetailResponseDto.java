package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDetailResponseDto {

    private Long id;
    private String userName;
    private String title;
    private String content;
    private Category category;
    private String defaultPostThumbnail;
    private LocalDateTime modifiedDate;
    private Long likes;

    public PostDetailResponseDto(Post post){
        this.id = post.getId();
        this.userName = post.getUser().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.defaultPostThumbnail = post.getDefaultPostThumbnail();
        this.modifiedDate = post.getModifiedDate();
        this.likes = post.getLikesPost().getLikes();
    }

    @Builder
    public PostDetailResponseDto(Long id, String userName, String title, String content, Category category, String defaultPostThumbnail, LocalDateTime modifiedDate, Long likes) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.category = category;
        this.defaultPostThumbnail = defaultPostThumbnail;
        this.modifiedDate = modifiedDate;
        this.likes = likes;
    }
}
