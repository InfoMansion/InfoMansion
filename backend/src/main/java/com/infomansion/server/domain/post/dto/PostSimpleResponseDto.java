package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostSimpleResponseDto {

    private Long id;
    private String title;
    private String content;
    private CategoryMapperValue category;
    private String defaultPostThumbnail;
    private LocalDateTime modifiedDate;
    private Long likes;

    public PostSimpleResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = new CategoryMapperValue(post.getCategory());
        this.defaultPostThumbnail = post.getDefaultPostThumbnail();
        this.modifiedDate = post.getModifiedDate();
        this.likes = post.getLikesPost().getLikes();
    }

    @Builder
    public PostSimpleResponseDto(Long id, String title, String content, CategoryMapperValue category, String defaultPostThumbnail, LocalDateTime modifiedDate, Long likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.defaultPostThumbnail = defaultPostThumbnail;
        this.modifiedDate = modifiedDate;
        this.likes = likes;
    }

    public static PostSimpleResponseDto toDto(Post post) {
        return PostSimpleResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(new CategoryMapperValue(post.getCategory()))
                .defaultPostThumbnail(post.getDefaultPostThumbnail())
                .modifiedDate(post.getModifiedDate())
                .likes((long) post.getUserLikePostList().size())
                .build();
    }
}
