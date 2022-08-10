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
    private boolean followFlag;


    @Builder
    public PostDetailResponseDto(Long id, String userName, String title, String content, Category category, String defaultPostThumbnail, LocalDateTime modifiedDate, Long likes, boolean followFlag) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.category = category;
        this.defaultPostThumbnail = defaultPostThumbnail;
        this.modifiedDate = modifiedDate;
        this.likes = likes;
        this.followFlag = followFlag;
    }


    public static PostDetailResponseDto toDto(Post post, boolean followFlag){
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .userName(post.getUser().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .defaultPostThumbnail(post.getDefaultPostThumbnail())
                .modifiedDate(post.getModifiedDate())
                .likes((long) post.getUserLikePostList().size())
                .followFlag(followFlag)
                .build();
    }
}
